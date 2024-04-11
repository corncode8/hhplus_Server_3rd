package hhplus.serverjava.domain.seat.infrastructure;

import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.seat.repository.SeatReaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Slf4j
@Repository
@RequiredArgsConstructor
public class SeatCoreReaderRepository implements SeatReaderRepository {

    private final SeatJPARepository seatJPARepository;

    @Override
    public List<Seat> findAvailableSeats(Long concertId, LocalDateTime targetDate, Seat.State state) {
        return seatJPARepository.findAvailableSeats(concertId, targetDate, state);
    }

    @Override
    public Optional<Seat> findAvailableSeat(Long concertOptionId, LocalDateTime targetDate, Seat.State state, int seatNum) {
        return seatJPARepository.findAvailableSeat(concertOptionId, targetDate, state, seatNum);

    }

    @Override
    public Optional<Seat> findSeatById(Long seatId) {
        return seatJPARepository.findById(seatId);
    }
}
