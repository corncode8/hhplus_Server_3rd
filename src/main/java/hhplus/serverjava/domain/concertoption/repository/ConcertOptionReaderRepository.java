package hhplus.serverjava.domain.concertoption.repository;

import hhplus.serverjava.domain.concertoption.entity.ConcertOption;

import java.util.List;

public interface ConcertOptionReaderRepository {

    List<ConcertOption> findConcertOption(Long concertId);
}
