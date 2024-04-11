package hhplus.serverjava.domain.seat.repository;

import hhplus.serverjava.domain.seat.entity.Seat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeatReaderRepository {

    List<Seat> findAvailableSeats(Long concertId, LocalDateTime targetDate, Seat.State state);

    Optional<Seat> findAvailableSeat(Long concertOptionId, LocalDateTime targetDate, Seat.State state, int seatNum);

    Optional<Seat> findSeatById(Long seatId);
}
