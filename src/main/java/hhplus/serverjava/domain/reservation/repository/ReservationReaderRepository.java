package hhplus.serverjava.domain.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import hhplus.serverjava.domain.reservation.entity.Reservation;

public interface ReservationReaderRepository {

	Optional<Reservation> findReservation(Long reservationId);

	List<Reservation> findExpiredReservaions(LocalDateTime now);
}
