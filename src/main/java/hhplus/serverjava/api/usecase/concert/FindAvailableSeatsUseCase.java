package hhplus.serverjava.api.usecase.concert;


import hhplus.serverjava.api.dto.response.seat.GetSeatsRes;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionReader;
import hhplus.serverjava.domain.seat.components.SeatReader;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class FindAvailableSeatsUseCase {

    private final SeatReader seatReader;
    private final ConcertOptionReader concertOptionReader;

    // 예약 가능한 좌석 조회 TODO: 수정必
    public GetSeatsRes execute(Long concertId, LocalDate targetDate) {

        List<Seat> availableSeats = seatReader.findAvailableSeats(concertId, targetDate, Seat.State.AVAILABLE);

        return new GetSeatsRes(availableSeats);
    }
}
