package hhplus.serverjava.domain.seat.components;

import org.springframework.stereotype.Component;

import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.seat.repository.SeatStoreRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SeatStore {

	private final SeatStoreRepository seatStoreRepository;

	public Seat save(Seat seat) {
		return seatStoreRepository.save(seat);
	}
}
