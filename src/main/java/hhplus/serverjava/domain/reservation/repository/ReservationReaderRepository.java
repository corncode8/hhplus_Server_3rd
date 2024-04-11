package hhplus.serverjava.domain.reservation.repository;

import hhplus.serverjava.domain.reservation.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationReaderRepository {

    Optional<Reservation> findReservation(Long reservationId);
    List<Reservation> findExpiredReservaions(LocalDateTime now);
}
