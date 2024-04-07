package hhplus.serverjava.domain.seat.infrastructure;

import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.seat.repository.SeatReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeatCoreReaderRepository implements SeatReaderRepository {

    private SeatJPARepository seatJPARepository;

    @Override
    public List<Seat> findAvailableSeats(Long concertId, LocalDateTime targetDate, Seat.State state) {
        return seatJPARepository.findAvailableSeats(concertId, targetDate, state);
    }

    @Override
    public Seat findAvailableSeat(Long concertId, LocalDateTime targetDate, Seat.State state, int seatNum) {
        return seatJPARepository.findAvailableSeat(concertId, targetDate, state, seatNum);
    }
}
