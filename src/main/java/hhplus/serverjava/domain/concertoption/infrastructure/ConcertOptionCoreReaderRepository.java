package hhplus.serverjava.domain.concertoption.infrastructure;

import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.repository.ConcertOptionReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class ConcertOptionCoreReaderRepository implements ConcertOptionReaderRepository {

    private final ConcertOptionJPARepository concertOptionJPARepository;

    @Override
    public List<ConcertOption> findConcertOptionList(Long concertId) {
        return concertOptionJPARepository.findByConcert_Id(concertId);
    }

    @Override
    public Optional<ConcertOption> findConcertOption(Long concertId, LocalDateTime concertAt) {
        return concertOptionJPARepository.findByConcert_IdAndConcertAt(concertId, concertAt);
    }

    public Optional<Concert> findConcert(Long concertOptionId) {
        return concertOptionJPARepository.findConcert(concertOptionId);
    }
}
