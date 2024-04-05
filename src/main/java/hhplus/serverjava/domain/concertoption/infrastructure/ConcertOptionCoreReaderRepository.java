package hhplus.serverjava.domain.concertoption.infrastructure;

import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.repository.ConcertOptionReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertOptionCoreReaderRepository implements ConcertOptionReaderRepository {

    private ConcertOptionJPARepository concertOptionJPARepository;

    @Override
    public List<ConcertOption> findConcertOption(Long concertId) {
        return concertOptionJPARepository.findByConcert_Id(concertId);
    }
}
