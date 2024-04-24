package hhplus.serverjava.api.seat.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.domain.seat.components.SeatReader;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatReservedUseCase {

	private final SeatReader seatReader;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Seat setReserved(Long concertOptionId, LocalDateTime dateTime, int seatNum) {

		// 예약 가능한 좌석 조회
		Seat seat = seatReader.findAvailableSeat(concertOptionId, dateTime, Seat.State.AVAILABLE, seatNum);

		// 좌석 예약상태로 변경, 임시 배정시간 5분 Set
		seat.setReserved();

		return seat;
	}
}
