package hhplus.serverjava.domain.seat.infrastructure;

import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.seat.repository.SeatReaderRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class SeatCoreReaderRepository implements SeatReaderRepository {

    private SeatJPARepository seatJPARepository;

    @Override
    public List<Seat> findAvailableSeats(Long concertId, LocalDate targetDate, Seat.State state) {
        return seatJPARepository.findAvailableSeats(concertId, targetDate, state);
    }
}
