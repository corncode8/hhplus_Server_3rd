package hhplus.serverjava.domain.concertoption.components;

import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.repository.ConcertOptionReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertOptionReader {

    private final ConcertOptionReaderRepository concertOptionReaderRepository;

    public List<ConcertOption> findConcertOptionList(Long concertId) {
        return concertOptionReaderRepository.findConcertOptionList(concertId);
    }

    public ConcertOption findConcertOption(Long concertId, LocalDateTime concertAt) {
        return concertOptionReaderRepository.findConcertOption(concertId, concertAt);
    }

    public Concert findConcert(Long concertOptionId) {
        return concertOptionReaderRepository.findConcert(concertOptionId);
    }
}
