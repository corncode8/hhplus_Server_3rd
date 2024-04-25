package hhplus.serverjava.domain.concert.infrastructure;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concert.repository.ConcertStoreRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ConcertCoreStoreRepository implements ConcertStoreRepository {

	private final ConcertJpaRepository concertJpaRepository;

	@Override
	public Concert save(Concert concert) {
		return concertJpaRepository.save(concert);
	}
}
