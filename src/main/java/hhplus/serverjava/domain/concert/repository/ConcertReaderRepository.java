package hhplus.serverjava.domain.concert.repository;

import hhplus.serverjava.domain.concert.entity.Concert;

import java.util.List;
import java.util.Optional;

public interface ConcertReaderRepository {

    Optional<Concert> findConcert(Long concertId);

    List<Concert> findConcertList(Concert.State state);

}
