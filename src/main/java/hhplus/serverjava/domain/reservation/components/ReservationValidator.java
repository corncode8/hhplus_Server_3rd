package hhplus.serverjava.domain.reservation.components;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.PAY_EXPIRED_SEAT;
import static hhplus.serverjava.api.support.response.BaseResponseStatus.PAY_INVALID_SEAT;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationValidator {

	public void validate(Reservation reservation) {
		Seat seat = reservation.getSeat();

		// 좌석이 만료되었는지 확인
		if (!LocalDateTime.now().isAfter(seat.getExpiredAt())) {
			throw new BaseException(PAY_EXPIRED_SEAT);
		}

		// 좌석이 선점되었는지 확인
		if (!seat.getStatus().equals(Seat.State.AVAILABLE)) {
			throw new BaseException(PAY_INVALID_SEAT);
		}

	}
}
