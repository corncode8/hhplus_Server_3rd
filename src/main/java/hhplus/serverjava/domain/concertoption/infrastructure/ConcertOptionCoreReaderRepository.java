package hhplus.serverjava.domain.concertoption.infrastructure;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.repository.ConcertOptionReaderRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ConcertOptionCoreReaderRepository implements ConcertOptionReaderRepository {
	private final ConcertOptionJpaRepository concertOptionJpaRepository;

	@Override
	public List<ConcertOption> findConcertOptionList(Long concertId) {
		return concertOptionJpaRepository.findByConcert_Id(concertId);
	}

	@Override
	public Optional<ConcertOption> findConcertOption(Long concertId, LocalDateTime concertAt) {
		return concertOptionJpaRepository.findByConcert_IdAndConcertAt(concertId, concertAt);
	}

	public Optional<Concert> findConcert(Long concertOptionId) {
		return concertOptionJpaRepository.findConcert(concertOptionId);
	}
}
