package hhplus.serverjava.domain.seat.repository;

import hhplus.serverjava.domain.seat.entity.Seat;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SeatReaderRepository {

    List<Seat> findAvailableSeats(Long concertId, LocalDateTime targetDate, Seat.State state);

    Seat findAvailableSeat(Long concertId, LocalDateTime targetDate, Seat.State state, int seatNum);
}
