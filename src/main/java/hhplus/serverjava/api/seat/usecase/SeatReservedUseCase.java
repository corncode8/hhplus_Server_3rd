package hhplus.serverjava.api.seat.usecase;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.RESERVED_SEAT;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.seat.components.SeatReader;
import hhplus.serverjava.domain.seat.components.SeatValidator;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatReservedUseCase {

	private final SeatReader seatReader;
	private final SeatValidator seatValidator;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Seat setReserved(Seat seat) {

		// 중복 예약 방지
		if (seatValidator.seatValidation(seat)) {
			// 좌석 예약상태로 변경, 임시 배정시간 5분 Set
			seat.setReserved();

			return seat;
		} else {
			throw new BaseException(RESERVED_SEAT);
		}
	}
}
