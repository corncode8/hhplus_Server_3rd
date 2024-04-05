package hhplus.serverjava.domain.concertoption.components;

import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.repository.ConcertOptionReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertOptionReader {

    private final ConcertOptionReaderRepository concertOptionReaderRepository;

    public List<ConcertOption> findConcertOption(Long concertId) {
        return concertOptionReaderRepository.findConcertOption(concertId);
    }
}
