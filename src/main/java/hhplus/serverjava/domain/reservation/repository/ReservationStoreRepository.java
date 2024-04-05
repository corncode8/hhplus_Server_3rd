package hhplus.serverjava.domain.reservation.repository;

import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.entity.Seat;

import java.time.LocalDate;
import java.util.List;

public interface ReservationStoreRepository {

    Reservation makeReservation(Reservation reservation);

    Reservation save(Reservation reservation);
}
