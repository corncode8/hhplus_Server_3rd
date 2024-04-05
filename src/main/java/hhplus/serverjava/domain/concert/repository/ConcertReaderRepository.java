package hhplus.serverjava.domain.concert.repository;

import hhplus.serverjava.domain.concert.entity.Concert;

public interface ConcertReaderRepository {

    Concert findConcert(Long concertId);
}
