package hhplus.serverjava.domain.concertoption.components;

import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.repository.ConcertOptionReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConcertOptionReader {

    private ConcertOptionReaderRepository concertOptionReaderRepository;

    public ConcertOption findConcertOption(Long concertId) {
        return concertOptionReaderRepository.findConcertOption(concertId);
    }
}
