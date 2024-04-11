package hhplus.serverjava.domain.concertoption.repository;

import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConcertOptionReaderRepository {

    List<ConcertOption> findConcertOptionList(Long concertId);

    Optional<ConcertOption> findConcertOption(Long concertId, LocalDateTime concertAt);

    Optional<Concert> findConcert(Long concertOptionId);
}
