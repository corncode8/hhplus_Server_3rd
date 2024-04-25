package hhplus.serverjava.domain.seat.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SeatTest {
	@DisplayName("setReserved테스트")
	@Test
	void setReservedTest() {
		//given
		Long testId = 1L;

		Seat seat = new Seat(testId, 50, 50000);

		//when
		LocalDateTime testDateTime = LocalDateTime.now().plusMinutes(5);
		seat.setReserved();

		//then
		assertEquals(Seat.State.RESERVED, seat.getStatus());
		assertTrue(Math.abs(Duration.between(testDateTime, seat.getExpiredAt()).getSeconds()) < 5);
	}

	@DisplayName("setAvailable테스트")
	@Test
	void setAvailableTest() {
		//given
		Long testId = 1L;

		Seat seat = new Seat(testId, 50, 50000);

		//when
		seat.setAvailable();

		//then
		assertEquals(Seat.State.AVAILABLE, seat.getStatus());
	}

}
