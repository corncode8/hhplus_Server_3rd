package hhplus.serverjava.domain.reservation.components;

import java.util.List;

import org.springframework.stereotype.Component;

import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.reservation.repository.ReservationStoreRepository;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationStore {
	private final ReservationStoreRepository repository;

	public Reservation save(Reservation reservation) {
		return repository.save(reservation);
	}

	// 좌석 활성화 + 예약 취소 로직
	public void expireReservation(List<Reservation> expiredReservaions) {
		if (!expiredReservaions.isEmpty()) {
			for (Reservation reservation : expiredReservaions) {
				Seat seat = reservation.getSeat();

				// 좌석 활성화
				seat.setAvailable();
				// 예약 취소
				reservation.setCancelled();
			}
		}
	}
}
