package hhplus.serverjava.domain.concert.components;

import org.springframework.stereotype.Component;

import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concert.repository.ConcertStoreRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConcertStore {

	private final ConcertStoreRepository concertStoreRepository;

	public Concert save(Concert concert) {
		return concertStoreRepository.save(concert);
	}
}
