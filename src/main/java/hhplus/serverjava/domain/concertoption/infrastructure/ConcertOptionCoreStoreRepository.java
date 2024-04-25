package hhplus.serverjava.domain.concertoption.infrastructure;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.repository.ConcertOptionStoreRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ConcertOptionCoreStoreRepository implements ConcertOptionStoreRepository {
	private final ConcertOptionJpaRepository concertOptionJpaRepository;

	@Override
	public ConcertOption save(ConcertOption concertOption) {
		return concertOptionJpaRepository.save(concertOption);
	}
}
