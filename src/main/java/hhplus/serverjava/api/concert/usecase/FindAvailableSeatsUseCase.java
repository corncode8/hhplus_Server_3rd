package hhplus.serverjava.api.concert.usecase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.seat.response.GetSeatsResponse;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionReader;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.seat.components.SeatReader;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindAvailableSeatsUseCase {

	private final SeatReader seatReader;
	private final ConcertOptionReader concertOptionReader;

	// 예약 가능한 좌석 조회
	public GetSeatsResponse execute(Long concertId, String targetDate) {

		LocalDateTime parse = LocalDateTime.parse(targetDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

		// 콘서트 옵션 조회
		ConcertOption concertOption = concertOptionReader.findConcertOption(concertId, parse);

		// 예약 가능한 좌석 조회
		List<Seat> availableSeats = seatReader.findAvailableSeats(concertId, parse, Seat.State.AVAILABLE);

		return new GetSeatsResponse(concertOption.getId(), availableSeats);
	}
}
