package hhplus.serverjava.domain.concert.repository;

import hhplus.serverjava.domain.concert.entity.Concert;

import java.util.List;

public interface ConcertReaderRepository {

    Concert findConcert(Long concertId);

    List<Concert> findConcertList(Concert.State state);

}
