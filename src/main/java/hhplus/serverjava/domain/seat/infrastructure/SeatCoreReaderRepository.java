package hhplus.serverjava.domain.seat.infrastructure;

import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.seat.repository.SeatReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class SeatCoreReaderRepository implements SeatReaderRepository {

    private final SeatJPARepository seatJPARepository;

    @Override
    public List<Seat> findAvailableSeats(Long concertId, LocalDateTime targetDate, Seat.State state) {
        return seatJPARepository.findAvailableSeats(concertId, targetDate, state);
    }

    @Override
    public Seat findAvailableSeat(Long concertId, LocalDateTime targetDate, Seat.State state, int seatNum) {
        Seat availableSeat = seatJPARepository.findAvailableSeat(concertId, targetDate, state, seatNum);

        if (availableSeat == null) {
            throw new BaseException(EMPTY_SEAT_RESERVATION);
        }
        return availableSeat;
    }
}
