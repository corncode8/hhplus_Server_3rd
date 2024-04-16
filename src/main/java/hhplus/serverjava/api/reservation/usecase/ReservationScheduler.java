package hhplus.serverjava.api.reservation.usecase;

import hhplus.serverjava.domain.reservation.components.ReservationReader;
import hhplus.serverjava.domain.reservation.components.ReservationStore;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.componenets.UserValidator;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationScheduler {

    private final UserStore userStore;
    private final UserReader userReader;
    private final UserValidator userValidator;
    private final ReservationStore reservationStore;
    private final ReservationReader reservationReader;

    @Scheduled(fixedDelay = 60000)     // 1분마다 실행
    @Transactional
    public void reservationExpired() {
        // 좌석 5분 임시배정(스케줄러)
        // 좌석의 만료시간이 5분이 지났다면 해당 좌석 AVAILABLE로 수정후 예약 CANCELLED로 수정

        // 좌석이 만료된 예약 조회
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> expiredReservations = reservationReader.findExpiredReservaions(now);

        // 예약 만료 : 좌석 활성화 + 예약 취소
        reservationStore.ExpireReservation(expiredReservations);

        // PROCESSING 유저 List
        List<User> workingUsers = userReader.findUsersByStatus(User.State.PROCESSING);

        int plusUsersNum = 0;

        // 서비스에 입장한 후 10분이 지나도록 결제도 안하고 있다면 내보내준다
        // 서비스를 이용중인 유저가 100명 미만이라면 plusUsersNum++
        plusUsersNum += userValidator.UserSchedulerValidator(workingUsers, now, plusUsersNum);

        // WAIT 유저 List
        List<User> waitUsers = userReader.findUsersByStatus(User.State.WAITING);

        // plusUsersNum의 수만큼 status를 Processing으로 변경
        userStore.enterService(waitUsers, plusUsersNum);
    }

}
