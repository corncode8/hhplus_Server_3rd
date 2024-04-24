package hhplus.serverjava.domain.reservation.repository;

import hhplus.serverjava.domain.reservation.entity.Reservation;

public interface ReservationStoreRepository {

	Reservation save(Reservation reservation);
}
