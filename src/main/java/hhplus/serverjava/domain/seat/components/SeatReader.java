package hhplus.serverjava.domain.seat.components;

import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.seat.repository.SeatReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatReader {

    private SeatReaderRepository seatReaderRepository;

    public List<Seat> findAvailableSeats(Long concertId, LocalDate targetDate, Seat.State state) {
        return seatReaderRepository.findAvailableSeats(concertId, targetDate, state);
    }
}
