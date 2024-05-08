package hhplus.serverjava.api.support.scheduler.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.domain.concert.components.ConcertReader;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.queue.components.RedisQueueManager;
import hhplus.serverjava.domain.reservation.components.ReservationReader;
import hhplus.serverjava.domain.reservation.components.ReservationStore;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.componenets.UserValidator;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ShedulerServiceImpl implements SchedulerService {

	private final ReservationStore reservationStore;
	private final ReservationReader reservationReader;
	private final RedisQueueManager redisQueueManager;
	private final ConcertReader concertReader;
	private final UserValidator userValidator;
	private final UserStore userStore;
	private final UserReader userReader;

	// 좌석이 만료된 예약 확인
	@Override
	public Boolean findExpiredReservations(LocalDateTime now) {
		// 좌석이 만료된 예약 조회
		List<Reservation> expiredReservations = reservationReader.findExpiredReservaions(now);

		if (!expiredReservations.isEmpty()) {
			return true;
		}
		return false;
	}

	// 좌석이 만료된 예약 처리
	@Override
	public void expireReservations(LocalDateTime now) {
		// 좌석이 만료된 예약 조회
		List<Reservation> expiredReservations = reservationReader.findExpiredReservaions(now);

		if (!expiredReservations.isEmpty()) {
			// 예약 만료 : 좌석 활성화 + 예약 취소
			log.info("예약 만료 로직 실행");
			reservationStore.expireReservation(expiredReservations);
		}

	}

	// 서비스에 입장한 후 10분이 지나도록
	// 결제를 안하고 있는 유저가 있는지 확인
	@Override
	public Boolean findUserTimeValidation(LocalDateTime now) {
		// 서비스 이용중 유저 조회
		List<User> workingUsers = userReader.findUsersByStatus(User.State.PROCESSING);

		Boolean validation = userValidator.userActiveTimeValidator(workingUsers, now);

		return validation;
	}

	// 서비스에 입장한 후 10분이 지나도록
	// 결제를 안하고 있는 유저 만료 처리
	public void expireUsers(LocalDateTime now) {
		List<User> workingUsers = userReader.findUsersByStatus(User.State.PROCESSING);

		if (!workingUsers.isEmpty()) {
			for (User user : workingUsers) {
				// user의 status를 Done으로 변경
				if (now.isAfter(user.getUpdatedAt().plusMinutes(10))) {
					user.setDone();
				}
			}
		}
	}

	// 서비스를 이용중인 유저가 100명 미만인지 확인
	@Override
	public Boolean findWorkingUserNumValidation(LocalDateTime now) {
		// 서비스 이용중 유저 조회
		List<User> workingUsers = userReader.findUsersByStatus(User.State.PROCESSING);

		if (workingUsers.size() < 100) {
			return true;
		}
		return false;
	}

	// 티켓팅중인 콘서트의 worikngQueue가 100 미만이라면 return true
	@Override
	public Boolean findWorkingUserNumValidationV2() {
		List<Concert> concertList = concertReader.findConcertList(Concert.State.SHOWING);

		if (!concertList.isEmpty()) {
			for (Concert concert : concertList) {
				Long workingUserNum = redisQueueManager.findWorkingUserNum(concert.getId());
				if (workingUserNum < 100) {
					return true;
				}
			}
		}
		return false;
	}

	// 100명보다 부족한 만큼 대기유저 활성화
	@Override
	public void enterServiceUser() {
		// 서비스 이용중 유저
		List<User> workingUsers = userReader.findUsersByStatus(User.State.PROCESSING);

		// 대기중인 유저
		List<User> waitingUsers = userReader.findUsersByStatus(User.State.WAITING);

		int num = 0;

		num = 100 - workingUsers.size();
		log.info("num : {}", num);

		if (num > 0) {
			userStore.enterService(waitingUsers, num);
		}

	}

	// 티켓팅중인 콘서트의 WorkingQueue가 100명보다 부족한 만큼 대기열 유저 활성화
	@Override
	public void enterServiceUserV2() {
		List<Concert> concertList = concertReader.findConcertList(Concert.State.SHOWING);

		for (Concert concert : concertList) {
			Long workingUserNum = redisQueueManager.findWorkingUserNum(concert.getId());
			if (workingUserNum < 100) {
				long enterNum = 100 - workingUserNum;
				// waitingQueue에서 우선순위가 높은 유저 제거
				List<String> userList = redisQueueManager.popUserFromWatingQueue(concert.getId(), enterNum);

				// waitingQueue에서 제거된 유저 WORKING으로 변경 후 WorkingQueue에 추가
				int i = redisQueueManager.addWorkingQueue(concert.getId(), userList);
			}
		}
	}
}
