package hhplus.serverjava.api.usecase.payment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.api.payment.request.PostPayRequest;
import hhplus.serverjava.api.payment.response.PostPayResponse;
import hhplus.serverjava.api.payment.usecase.PaymentUseCase;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.payment.components.PaymentStore;
import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.reservation.components.ReservationReader;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserValidator;
import hhplus.serverjava.domain.user.entity.User;

@ExtendWith(MockitoExtension.class)
public class PaymentUseCaseTest {

	@Mock
	PaymentStore paymentStore;

	@Mock
	ReservationReader reservationReader;

	@Mock
	UserReader userReader;

	@Mock
	UserValidator userValidator;

	@InjectMocks
	PaymentUseCase paymentUseCase;

	@DisplayName("결제 테스트")
	@Test
	void test() {
		//given
		Long id = 1L;
		int payAmount = 50000;

		User user = new User(id, 5000000L);

		Concert concert = Concert.builder()
			.id(id)
			.name("마크툽 콘서트")
			.artist("마크툽")
			.build();

		ConcertOption concertOption = ConcertOption.builder()
			.id(id)
			.concertAt(LocalDateTime.now())
			.build();

		Seat seat = Seat.builder()
			.id(id)
			.price(50000)
			.seatNum(15)
			.build();

		Reservation reservation = Reservation.builder()
			.seatNum(seat.getSeatNum())
			.concertArtist(concert.getArtist())
			.concertName(concert.getName())
			.reservedPrice(seat.getPrice())
			.concertAt(concertOption.getConcertAt())
			.user(user)
			.seat(seat)
			.build();

		Payment payment = Payment.builder()
			.id(id)
			.payAmount((long)payAmount)
			.reservation(reservation)
			.build();

		when(reservationReader.findReservation(reservation.getId())).thenReturn(reservation);
		when(paymentStore.save(any(Payment.class))).thenReturn(payment);
		when(userReader.findUser(user.getId())).thenReturn(user);

		PostPayRequest request = new PostPayRequest(reservation.getId(), payAmount);

		//when
		PostPayResponse result = paymentUseCase.execute(request, user.getId());

		//then
		assertNotNull(result);

		assertEquals(result.getPayAmount(), payment.getPayAmount());
		assertEquals(result.getReservationId(), reservation.getId());

		// 예약 상태 PAID
		assertEquals(Reservation.State.PAID, payment.getReservation().getStatus());
		// 유저 상태 DONE
		assertEquals(User.State.DONE, payment.getReservation().getUser().getStatus());
	}
}
