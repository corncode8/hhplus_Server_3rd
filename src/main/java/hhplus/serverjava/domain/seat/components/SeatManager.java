package hhplus.serverjava.domain.seat.components;

import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.seat.repository.SeatReaderRepository;
import hhplus.serverjava.domain.seat.repository.SeatStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatManager {

    private final SeatReaderRepository seatReaderRepository;
    private final SeatStoreRepository seatStoreRepository;

    public List<Seat> findAvailableSeats(Long concertId, LocalDateTime tagetDate, Seat.State state) {
        return seatReaderRepository.findAvailableSeats(concertId, tagetDate, state);
    }

    public Seat save(Seat seat) {
        return seatStoreRepository.save(seat);
    }
}
