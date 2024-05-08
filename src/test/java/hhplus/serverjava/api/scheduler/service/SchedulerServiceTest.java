package hhplus.serverjava.api.scheduler.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import hhplus.serverjava.api.support.scheduler.service.SchedulerService;
import hhplus.serverjava.domain.concert.components.ConcertStore;
import hhplus.serverjava.domain.concert.entity.Concert;
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
	// @checkstyle:off

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
	@Autowired
	private ConcertStore concertStore;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private ZSetOperations<String, String> zSetOperations;

	@Container
	private static GenericContainer mySqlContainer = new MySQLContainer("mysql:8.0")
		.withReuse(true);

	@Container
	private static GenericContainer redisContainer = new GenericContainer(DockerImageName.parse("redis:latest"))
		.withExposedPorts(6379);

	@DynamicPropertySource
	static void registerPgProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.redis.host", () -> redisContainer.getHost());
		registry.add("spring.redis.port", () -> String.valueOf(redisContainer.getMappedPort(6379)));
	}

	private final String WAITING_KEY = "waiting:concert:";
	private final String WORKING_KEY = "working:concert:";

	@BeforeEach
	void setUp() {
		zSetOperations = redisTemplate.opsForZSet();
	}

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

	@DisplayName("findWorkingUserNumValidationV2 테스트(true)")
	@Test
	void findWorkingUserNumValidationV2Test() {
		//given
		Concert concert = Concert.builder()
			.artist("IU")
			.name("IU Concert")
			.build();
		concertStore.save(concert);
		String key = WORKING_KEY + concert.getId();

		for (int i = 0; i < 99; i++) {
			zSetOperations.add(key, String.valueOf(i + 1L), System.currentTimeMillis());
		}

		//when
		Boolean result = schedulerService.findWorkingUserNumValidationV2();

		//then
		assertNotNull(result);

		// 티켓팅중인 콘서트의 worikngQueue가 100 미만이라면 return true
		assertEquals(true, result);
	}

	@DisplayName("findWorkingUserNumValidationV2 테스트(false)")
	@Test
	void findWorkingUserNumValidationV2FalseTest() {
		//given
		Concert concert = Concert.builder()
			.artist("IU")
			.name("IU Concert")
			.build();
		concertStore.save(concert);
		String key = WORKING_KEY + concert.getId();

		for (int i = 0; i < 100; i++) {
			zSetOperations.add(key, String.valueOf(i + 1L), System.currentTimeMillis());
		}

		//when
		Boolean result = schedulerService.findWorkingUserNumValidationV2();

		//then
		assertNotNull(result);

		// 티켓팅중인 콘서트의 worikngQueue가 100 미만이라면 return true
		assertEquals(false, result);
	}

	// 티켓팅중인 콘서트의 WorkingQueue가 100명보다 부족한 만큼 대기열 유저 활성화
	@DisplayName("enterServiceUserV2 테스트")
	@Test
	void enterServiceUserV2Test() {
		//given
		Concert concert = Concert.builder()
			.artist("IU")
			.name("IU Concert")
			.build();
		concertStore.save(concert);
		String workingKey = WORKING_KEY + concert.getId();
		String waitingKey = WAITING_KEY + concert.getId();

		int userNum = 30;
		setUser(userNum);

		// 서비스 이용중인 유저 60명
		for (int i = 0; i < 60; i++) {
			zSetOperations.add(workingKey, String.valueOf(i + 50L), System.currentTimeMillis());
		}
		Long beforeWorkSize = zSetOperations.size(workingKey);

		// 대기중 유저 30명
		for (int i = 0; i < userNum; i++) {
			zSetOperations.add(waitingKey, String.valueOf(i + 1L), System.currentTimeMillis());
		}
		Long beforeWaitSize = zSetOperations.size(waitingKey);

		//when
		schedulerService.enterServiceUserV2();

		//then
		Long workSize = zSetOperations.size(workingKey);
		Long waitSize = zSetOperations.size(waitingKey);

		// 로직 실행 전 workingQueue, waitQueue size
		assertEquals(60, beforeWorkSize);
		assertEquals(30, beforeWaitSize);

		// 대기중 유저 + workingQueue
		assertEquals(beforeWorkSize + beforeWaitSize, workSize);
		assertEquals(0, waitSize);
	}

	void setUser(int num) {
		for (int i = 0; i < num; i++) {
			User user = User.builder()
				.name("testUser")
				.point(10L)
				.updatedAt(LocalDateTime.now())
				.build();
			userStore.save(user);
		}
	}

}
