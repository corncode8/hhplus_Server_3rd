package hhplus.serverjava.api.scheduler.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import hhplus.serverjava.api.support.scheduler.service.SchedulerService;
import hhplus.serverjava.domain.reservation.components.ReservationReader;
import hhplus.serverjava.domain.reservation.components.ReservationStore;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.components.SeatReader;
import hhplus.serverjava.domain.seat.components.SeatStore;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SchedulerServiceTest {
	@Autowired
	private ReservationStore reservationStore;
	@Autowired
	private ReservationReader reservationReader;
	@Autowired
	private UserStore userStore;
	@Autowired
	private UserReader userReader;
	@Autowired
	private SeatStore seatStore;
	@Autowired
	private SeatReader seatReader;
	@Autowired
	private SchedulerService schedulerService;

	@Container
	private static GenericContainer mySqlContainer = new MySQLContainer("mysql:8.0")
		.withReuse(true);

	@DisplayName("좌석이 만료된 예약 조회 테스트")
	@Test
	void findExpiredReservationsTest() {
		//given

		// 만료된 좌석,예약 생성
		Seat seat = Seat.builder()
			.seatNum(50)
			.price(5000)
			.build();
		seat.setReserved();
		seat.setExpiredAt(LocalDateTime.now().minusMinutes(10));
		seatStore.save(seat);

		Reservation reservation = Reservation.builder()
			.concertAt(LocalDateTime.now().plusDays(5))
			.reservedPrice(seat.getPrice())
			.seat(seat)
			.concertName("MAKTUB CONCERT")
			.concertArtist("MAKTUB")
			.seatNum(seat.getSeatNum())
			.build();
		reservationStore.save(reservation);

		//when
		Boolean result = schedulerService.findExpiredReservations(LocalDateTime.now());

		//then
		assertNotNull(result);
		assertEquals(true, result);
	}

	@DisplayName("좌석이 만료된 예약 좌석 활성화, 예약 취소 처리 테스트")
	@Test
	void expireReservationsTest() {
		//given

		// 만료된 좌석,예약 생성
		Seat seat = Seat.builder()
			.seatNum(50)
			.price(5000)
			.build();
		seat.setReserved();
		seat.setExpiredAt(LocalDateTime.now().minusMinutes(10));
		seatStore.save(seat);

		Reservation reservation = Reservation.builder()
			.concertAt(LocalDateTime.now().plusDays(5))
			.reservedPrice(seat.getPrice())
			.seat(seat)
			.concertName("MAKTUB CONCERT")
			.concertArtist("MAKTUB")
			.seatNum(seat.getSeatNum())
			.build();
		reservationStore.save(reservation);

		//when
		schedulerService.expireReservations(LocalDateTime.now());

		//then

		// seat이 만료되었다면 다시 AVAILABLE로 변했는지 검증
		Seat updSeat = seatReader.findSeatById(seat.getId());
		assertEquals(Seat.State.AVAILABLE, updSeat.getStatus());

		// 해당 예약이 취소처리가 되었는지 검증
		Reservation updReservaton = reservationReader.findReservation(reservation.getId());
		assertEquals(Reservation.State.CANCELLED, updReservaton.getStatus());
	}

	@DisplayName("10분동안 결제를 안하고 있는 유저 있는지 조회 테스트")
	@Test
	void findUserTimeValidationTest() {
		//given
		User user = User.builder()
			.name("testUsername")
			.point(500000L)
			.updatedAt(LocalDateTime.now())
			.build();
		user.setProcessing();
		user.setUpdatedAt(LocalDateTime.now().minusMinutes(15));
		userStore.save(user);

		//when
		Boolean result = schedulerService.findUserTimeValidation(LocalDateTime.now());

		//then
		assertNotNull(result);
		assertEquals(true, result);
	}

	@DisplayName("유저 만료 테스트")
	@Test
	void expireUsersTest() {
		//given
		User user = User.builder()
			.name("testUsername")
			.point(500000L)
			.updatedAt(LocalDateTime.now())
			.build();
		user.setProcessing();
		user.setUpdatedAt(LocalDateTime.now().minusMinutes(15));
		userStore.save(user);

		//when
		schedulerService.expireUsers(LocalDateTime.now());

		//then
		User updUser = userReader.findUser(user.getId());
		assertEquals(User.State.DONE, updUser.getStatus());
	}

	@DisplayName("서비스를 이용중인 유저가 100명 미만인지 확인 테스트(true)")
	@Test
	void test() {
		//given
		// 유저 99명 생성
		for (int i = 0; i < 99; i++) {
			User user = User.builder()
				.name("testUsername" + i)
				.point(0L)
				.updatedAt(LocalDateTime.now())
				.build();
			user.setProcessing();
			userStore.save(user);
		}

		//when
		Boolean result = schedulerService.findWorkingUserNumValidation(LocalDateTime.now());

		//then
		assertNotNull(result);
		// 현재 PROCESSING인 유저가 99명이기 때문에 true
		assertEquals(true, result);
	}

	@DisplayName("서비스를 이용중인 유저가 100명 미만인지 확인 테스트(false)")
	@Test
	void findWorkingUserNumValidationTest() {
		//given
		// 유저 110명 생성
		for (int i = 0; i < 110; i++) {
			User user = User.builder()
				.name("testUsername" + i)
				.point(0L)
				.updatedAt(LocalDateTime.now())
				.build();
			user.setProcessing();
			userStore.save(user);
		}

		//when
		Boolean result = schedulerService.findWorkingUserNumValidation(LocalDateTime.now());

		//then
		assertNotNull(result);
		// 현재 PROCESSING인 유저가 110명이기 때문에 false
		assertEquals(false, result);
	}

	@DisplayName("100명보다 부족한 만큼 대기유저 활성화 테스트")
	@Test
	void enterServiceUserTest() {
		//given
		// 활성화 유저 50명 생성
		for (int i = 0; i < 50; i++) {
			User user = User.builder()
				.name("testUsername" + i)
				.point(0L)
				.updatedAt(LocalDateTime.now())
				.build();
			user.setProcessing();
			userStore.save(user);
		}

		// 대기 유저 30명 생성
		for (int i = 0; i < 30; i++) {
			User user = User.builder()
				.name("waitUsername" + i)
				.point(0L)
				.updatedAt(LocalDateTime.now())
				.build();
			user.setWaiting();
			userStore.save(user);
		}

		//when
		schedulerService.enterServiceUser();

		//then

		// 서비스 이용중 유저 List
		List<User> workingUsers = userReader.findUsersByStatus(User.State.PROCESSING);

		// 서비스 이용중 유저의 수는 기존 활성화 유저(50) + 대기 유저(30)
		assertEquals(50 + 30, workingUsers.size());
	}
}
