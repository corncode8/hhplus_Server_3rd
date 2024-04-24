package hhplus.serverjava.domain.concertoption.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;

public interface ConcertOptionReaderRepository {

	List<ConcertOption> findConcertOptionList(Long concertId);

	Optional<ConcertOption> findConcertOption(Long concertId, LocalDateTime concertAt);

	Optional<Concert> findConcert(Long concertOptionId);
}
