package hhplus.serverjava.domain.reservation.components;

import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.reservation.repository.ReservationReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationReader {

    private final ReservationReaderRepository reservationReaderRepository;


    public Reservation findReservation(Long reservationId) {
        return reservationReaderRepository.findReservation(reservationId);
    }
    public List<Reservation> findExpiredReservaions(LocalDateTime now) {
        return reservationReaderRepository.findExpiredReservaions(now);
    }
}
