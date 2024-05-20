package hhplus.serverjava.api.usecase.payment;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_ENOUGH_POINT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import hhplus.serverjava.api.payment.request.PostPayRequest;
import hhplus.serverjava.api.payment.response.PostPayResponse;
import hhplus.serverjava.api.payment.usecase.PaymentUseCase;
import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.concert.components.ConcertStore;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionStore;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.reservation.components.ReservationReader;
import hhplus.serverjava.domain.reservation.components.ReservationStore;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.components.SeatStore;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
// @checkstyle:off
public class PaymentIntegrationTest {

	@Autowired
	private UserStore userStore;
	@Autowired
	private ConcertStore concertStore;
	@Autowired
	private ConcertOptionStore concertOptionStore;
	@Autowired
	private SeatStore seatStore;
	@Autowired
	private ReservationStore reservationStore;
	@Autowired
	private ReservationReader reservationReader;

	@Autowired
	private PaymentUseCase paymentUseCase;

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

	@BeforeEach
	void setUp() {
		User user = User.builder()
			.name("paymentTestUser")
			.point(50000L)
			.updatedAt(LocalDateTime.now())
			.build();
		user.setProcessing();
		userStore.save(user);

		Concert concert = Concert.builder()
			.name("마크툽 콘서트")
			.artist("마크툽")
			.build();
		concertStore.save(concert);

		ConcertOption concertOption = ConcertOption.builder()
			.concertAt(LocalDateTime.now())
			.build();
		concertOptionStore.save(concertOption);

		Seat seat = Seat.builder()
			.price(50000)
			.seatNum(15)
			.build();
		seatStore.save(seat);

		Reservation reservation = Reservation.builder()
			.seatNum(seat.getSeatNum())
			.concertArtist(concert.getArtist())
			.concertName(concert.getName())
			.reservedPrice(seat.getPrice())
			.concertAt(concertOption.getConcertAt())
			.user(user)
			.seat(seat)
			.build();
		reservationStore.save(reservation);
	}

	@DisplayName("결제 테스트")
	@Test
	void paymentTest() {
		//given
		Long testReservationId = 1L;
		int payAmount = 50000;

		PostPayRequest request = new PostPayRequest(testReservationId, payAmount, 1L);

		User newUser = new User(testReservationId, (long)payAmount, LocalDateTime.now(), "testname");
		userStore.save(newUser);

		//when
		PostPayResponse result = paymentUseCase.pay(request, newUser.getId());

		//then
		assertNotNull(result);
		assertEquals(payAmount, result.getPayAmount());
		assertEquals(testReservationId, result.getPayId());

		Reservation reservation = reservationReader.findReservation(result.getReservationId());
		System.out.println("reservation.getStatus() = " + reservation.getStatus());
	}

	@DisplayName("결제 테스트 실패 (잔액 부족)")
	@Test
	void paymentFailTest() {
		//given
		Long testReservationId = 1L;
		int payAmount = 5000000;

		PostPayRequest request = new PostPayRequest(testReservationId, payAmount, 1L);

		User newUser = new User(testReservationId, 50L, LocalDateTime.now(), "testname");
		userStore.save(newUser);

		//when & then
		BaseException exception = assertThrows(BaseException.class,
			() -> paymentUseCase.pay(request, newUser.getId()));
		assertEquals(NOT_ENOUGH_POINT.getMessage(), exception.getMessage());
	}
}
