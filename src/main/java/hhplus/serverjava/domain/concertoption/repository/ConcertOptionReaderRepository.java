package hhplus.serverjava.domain.concertoption.repository;

import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertOptionReaderRepository {

    List<ConcertOption> findConcertOptionList(Long concertId);

    ConcertOption findConcertOption(Long concertId, LocalDateTime concertAt);

    Concert findConcert(Long concertOptionId);
}
