package hhplus.serverjava.domain.concertoption.infrastructure;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.repository.ConcertOptionStoreRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ConcertOptionCoreStoreRepository implements ConcertOptionStoreRepository {

	private final ConcertOptionJPARepository concertOptionJPARepository;

	@Override
	public ConcertOption save(ConcertOption concertOption) {
		return concertOptionJPARepository.save(concertOption);
	}
}
