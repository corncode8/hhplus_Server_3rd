package hhplus.serverjava.domain.concertoption.repository;

import hhplus.serverjava.domain.concertoption.entity.ConcertOption;

public interface ConcertOptionReaderRepository {

    ConcertOption findConcertOption(Long concertId);
}
