package hhplus.serverjava.domain.concertoption.infrastructure;

import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.repository.ConcertOptionReaderRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConcertOptionCoreReaderRepository implements ConcertOptionReaderRepository {

    private ConcertOptionJPARepository concertOptionJPARepository;

    @Override
    public ConcertOption findConcertOption(Long concertId) {
        return concertOptionJPARepository.findByConcert_Id(concertId);
    }
}
