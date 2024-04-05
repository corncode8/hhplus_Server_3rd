package hhplus.serverjava.domain.reservation.components;

import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.reservation.repository.ReservationStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class ReservationStore {

    private final ReservationStoreRepository repository;

    public Reservation makeReservation(Reservation reservation) {
        return repository.makeReservation(reservation);
    }

    public Reservation save(Reservation reservation) {
        return repository.save(reservation);
    }
}
