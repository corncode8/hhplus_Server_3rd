package hhplus.serverjava.domain.reservation.repository;

import hhplus.serverjava.domain.reservation.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationReaderRepository {

    List<Reservation> findExpiredReservaions(LocalDateTime now);
}
