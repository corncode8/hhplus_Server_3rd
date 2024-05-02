package hhplus.serverjava.domain.seat.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.domain.seat.entity.Seat;

@ExtendWith(MockitoExtension.class)
public class SeatValidatorTest {

	@InjectMocks
	SeatValidator seatValidator;

	// 만료시간이 현재시간보다 이전이라면 좌석 선점
	@DisplayName("seatValidation 테스트")
	@Test
	void trueTest() {
		//given
		Seat seat = new Seat(1L, 10, 5000);
		seat.setExpiredAt(LocalDateTime.now().minusMinutes(1));

		//when
		Boolean result = seatValidator.seatValidation(seat);

		//then
		assertNotNull(result);
		assertEquals(result, true);
	}

	// 만료시간이 현재시간보다 이후라면 좌석 선점 불가능
	@DisplayName("seatValidation false 테스트")
	@Test
	void falseTest() {
		//given
		Seat seat = new Seat(1L, 10, 5000);
		seat.setExpiredAt(LocalDateTime.now().plusMinutes(5));

		//when
		Boolean result = seatValidator.seatValidation(seat);

		//then
		assertNotNull(result);
		assertEquals(result, false);
	}
}
