package hhplus.serverjava.domain.concert.infrastructure;

import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concert.repository.ConcertReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class ConcertCoreReaderRepository implements ConcertReaderRepository {

    private final ConcertJPARepository concertJPARepository;
    @Override
    public Optional<Concert> findConcert(Long concertId) {
        return concertJPARepository.findById(concertId);
    }

    @Override
    public List<Concert> findConcertList(Concert.State state) {
        return concertJPARepository.findConcertsByStatus(state);
    }
}
