package hhplus.serverjava.domain.seat.infrastructure;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.seat.repository.SeatReaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SeatCoreReaderRepository implements SeatReaderRepository {
	private final SeatJpaRepository seatJpaRepository;

	@Override
	public List<Seat> findAvailableSeats(Long concertId, LocalDateTime targetDate, Seat.State state) {
		return seatJpaRepository.findAvailableSeats(concertId, targetDate, state);
	}

	@Override
	public Optional<Seat> findAvailableSeat(Long concertOptionId, LocalDateTime targetDate, Seat.State state,
		int seatNum) {
		return seatJpaRepository.findAvailableSeat(concertOptionId, targetDate, state, seatNum);

	}

	@Override
	public Optional<Seat> findSeatById(Long seatId) {
		return seatJpaRepository.findById(seatId);
	}
}
