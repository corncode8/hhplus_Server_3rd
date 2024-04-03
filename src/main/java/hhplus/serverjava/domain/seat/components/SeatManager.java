package hhplus.serverjava.domain.seat.components;

import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.seat.repository.SeatReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatManager {

    private SeatReaderRepository seatReaderRepository;

    public List<Seat> findAvailableSeats(Long concertId, LocalDate tagetDate, Seat.State state) {
        return seatReaderRepository.findAvailableSeats(concertId, tagetDate, state);
    }
}
