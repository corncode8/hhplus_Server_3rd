package hhplus.serverjava.domain.concert.repository;

import java.util.List;
import java.util.Optional;

import hhplus.serverjava.domain.concert.entity.Concert;

public interface ConcertReaderRepository {

	Optional<Concert> findConcert(Long concertId);

	List<Concert> findConcertList(Concert.State state);

}
