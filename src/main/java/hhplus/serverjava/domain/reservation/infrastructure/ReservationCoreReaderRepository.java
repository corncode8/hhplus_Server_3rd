package hhplus.serverjava.domain.reservation.infrastructure;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.reservation.repository.ReservationReaderRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReservationCoreReaderRepository implements ReservationReaderRepository {
	private final ReservationJpaRepository repository;

	@Override
	public Optional<Reservation> findReservation(Long reservationId) {
		return repository.findById(reservationId);
	}

	@Override
	public List<Reservation> findExpiredReservaions(LocalDateTime now) {
		return repository.findExpiredReservations(now);
	}
}
