package hhplus.serverjava.domain.reservation.components;

import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.reservation.repository.ReservationReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.INVALID_RESERVATION;

@Component
@RequiredArgsConstructor
public class ReservationReader {

    private final ReservationReaderRepository reservationReaderRepository;

    public Reservation findReservation(Long reservationId) {
        return reservationReaderRepository.findReservation(reservationId)
                .orElseThrow(() -> new BaseException(INVALID_RESERVATION));
    }

    // 스케줄러 로직
    public List<Reservation> findExpiredReservaions(LocalDateTime now) {
        return reservationReaderRepository.findExpiredReservaions(now);
    }
}
