package hhplus.serverjava.domain.concert.components;

import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concert.repository.ConcertReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertReader {

    private final ConcertReaderRepository concertReaderRepository;

    public Concert findConcert(Long concertId) {
        return concertReaderRepository.findConcert(concertId);
    }

    public List<Concert> findConcertList(Concert.State state) {
        return concertReaderRepository.findConcertList(state);
    }
}
