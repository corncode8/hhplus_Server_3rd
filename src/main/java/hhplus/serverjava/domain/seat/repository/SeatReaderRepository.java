package hhplus.serverjava.domain.seat.repository;

import hhplus.serverjava.domain.seat.entity.Seat;

import java.time.LocalDate;
import java.util.List;

public interface SeatReaderRepository {

    List<Seat> findAvailableSeats(Long concertId, LocalDate targetDate, Seat.State state);
}
