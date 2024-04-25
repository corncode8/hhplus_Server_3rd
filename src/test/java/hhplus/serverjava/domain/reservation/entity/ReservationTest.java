package hhplus.serverjava.domain.reservation.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.domain.user.entity.User;

@ExtendWith(MockitoExtension.class)
public class ReservationTest {
	@Mock
	User user;

	@DisplayName("setPaid테스트")
	@Test
	void setPaidTest() {
		//given
		LocalDateTime testDateTime = LocalDateTime.now();

		Reservation reservation = Reservation.builder()
			.seatNum(10)
			.reservedPrice(50000)
			.concertAt(testDateTime)
			.concertName("MAKTUB CONCERT")
			.concertArtist("MAKTUB")
			.user(user)
			.build();

		//when
		reservation.setPaid();

		//then
		assertEquals(Reservation.State.PAID, reservation.status);
		verify(user).setDone();
	}

	@DisplayName("setCancelled테스트")
	@Test
	void setCancelledTest() {
		//given
		LocalDateTime testDateTime = LocalDateTime.now();
		Reservation reservation = Reservation.builder()
			.seatNum(10)
			.reservedPrice(50000)
			.concertAt(testDateTime)
			.concertName("MAKTUB CONCERT")
			.concertArtist("MAKTUB")
			.build();

		//when
		reservation.setCancelled();

		//then
		assertEquals(reservation.status, Reservation.State.CANCELLED);
	}

}
