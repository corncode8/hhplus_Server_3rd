package hhplus.serverjava.api.usecase.payment;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import hhplus.serverjava.api.payment.request.PostPayRequest;
import hhplus.serverjava.api.payment.response.PostPayResponse;
import hhplus.serverjava.api.payment.usecase.PaymentUseCase;
import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.concert.components.ConcertStore;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionStore;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.reservation.components.ReservationStore;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.components.SeatStore;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;

@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
	private PaymentUseCase paymentUseCase;

	Scheduler scheduler;

	@BeforeEach
	void setUp() throws SchedulerException {
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

		PostPayRequest request = new PostPayRequest(testReservationId, payAmount);

		User newUser = new User(testReservationId, (long)payAmount, LocalDateTime.now(), "testname");
		userStore.save(newUser);

		//when
		PostPayResponse result = paymentUseCase.execute(request, newUser.getId());

		//then
		assertNotNull(result);
		assertEquals(payAmount, result.getPayAmount());
		assertEquals(testReservationId, result.getPayId());
	}

	@DisplayName("결제 테스트 실패 (잔액 부족)")
	@Test
	void paymentFailTest() {
		//given
		Long testReservationId = 1L;
		int payAmount = 5000000;

		PostPayRequest request = new PostPayRequest(testReservationId, payAmount);

		User newUser = new User(testReservationId, 50L, LocalDateTime.now(), "testname");
		userStore.save(newUser);

		//when & then
		BaseException exception = assertThrows(BaseException.class,
			() -> paymentUseCase.execute(request, newUser.getId()));
		assertEquals(NOT_ENOUGH_POINT.getMessage(), exception.getMessage());
	}
}
