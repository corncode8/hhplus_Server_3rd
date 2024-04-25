package hhplus.serverjava.domain.seat.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import hhplus.serverjava.domain.seat.entity.Seat;

public interface SeatReaderRepository {
	List<Seat> findAvailableSeats(Long concertId, LocalDateTime targetDate, Seat.State state);

	Optional<Seat> findAvailableSeat(Long concertOptionId, LocalDateTime targetDate, Seat.State state, int seatNum);

	Optional<Seat> findSeatById(Long seatId);
}
