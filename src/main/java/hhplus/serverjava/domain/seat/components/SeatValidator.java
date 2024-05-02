package hhplus.serverjava.domain.seat.components;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SeatValidator {

	public Boolean seatValidation(Seat seat) {
		// 중복 예약 방지
		if (seat.getExpiredAt().isBefore(LocalDateTime.now())) {
			return true;
		}

		return false;
	}
}
