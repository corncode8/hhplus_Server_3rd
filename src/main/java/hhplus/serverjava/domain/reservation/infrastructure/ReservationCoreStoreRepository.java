package hhplus.serverjava.domain.reservation.infrastructure;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.reservation.repository.ReservationStoreRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReservationCoreStoreRepository implements ReservationStoreRepository {

	private final ReservationJPARepository repository;

	@Override
	public Reservation save(Reservation reservation) {
		return repository.save(reservation);
	}

}
