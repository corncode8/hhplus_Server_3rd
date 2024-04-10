package hhplus.serverjava.domain.seat.components;

import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.seat.repository.SeatReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatReader {

    private final SeatReaderRepository seatReaderRepository;

    public List<Seat> findAvailableSeats(Long concertId, LocalDateTime targetDate, Seat.State state) {
        return seatReaderRepository.findAvailableSeats(concertId, targetDate, state);
    }


    public Seat findAvailableSeat(Long concertOptionId, LocalDateTime targetDate, Seat.State state, int seatNum) {
        return seatReaderRepository.findAvailableSeat(concertOptionId, targetDate, state, seatNum);
    }

    public Seat findSeatById(Long seatId) {
        return seatReaderRepository.findSeatById(seatId);
    }
}
