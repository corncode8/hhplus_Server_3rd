package hhplus.serverjava.api.usecase.reservation;

import hhplus.serverjava.domain.reservation.components.ReservationReader;
import hhplus.serverjava.domain.reservation.components.ReservationStore;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.components.SeatStore;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationScheduler {

    private final SeatStore seatStore;
    private final ReservationStore reservationStore;
    private final ReservationReader reservationReader;

    @Scheduled(fixedDelay = 60000)     // 1분마다 실행
    @Transactional
    public void reservationExpired() {
        // 좌석 5분 임시배정(스케줄러)
        // 좌석의 만료시간이 5분이 지났다면 해당 좌석 AVAILABLE로 수정후 예약 CANCELLED로 수정

        // 좌석이 만료된 예약 조회
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> expiredReservaions = reservationReader.findExpiredReservaions(now);

        for (Reservation reservation : expiredReservaions) {
            Seat seat = reservation.getSeat();

            seat.setAvailable();
            seatStore.save(seat);

            reservation.setCancelled();
            reservationStore.save(reservation);
        }

        // TODO:
    }
}
