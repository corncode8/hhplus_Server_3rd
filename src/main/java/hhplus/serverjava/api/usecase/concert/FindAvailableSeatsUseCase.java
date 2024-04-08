package hhplus.serverjava.api.usecase.concert;


import hhplus.serverjava.api.dto.response.seat.GetSeatsRes;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionReader;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.seat.components.SeatReader;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class FindAvailableSeatsUseCase {

    private final SeatReader seatReader;
    private final ConcertOptionReader concertOptionReader;

    // 예약 가능한 좌석 조회
    public GetSeatsRes execute(Long concertId, LocalDateTime targetDate) {

        // 콘서트 조회
        ConcertOption concertOption = findOption(concertId, targetDate);

        // 예약 가능한 좌석 조회
        List<Seat> availableSeats = getAvailableSeatList(concertId, targetDate, Seat.State.AVAILABLE);

        return new GetSeatsRes(concertOption.getId(), availableSeats);
    }

    private ConcertOption findOption(Long concertId, LocalDateTime date) {
        return concertOptionReader.findConcertOption(concertId, date);
    }

    private List<Seat> getAvailableSeatList(Long concertId, LocalDateTime date, Seat.State state) {
        return seatReader.findAvailableSeats(concertId, date, state);
    }
}
