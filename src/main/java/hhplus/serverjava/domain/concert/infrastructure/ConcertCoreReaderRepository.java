package hhplus.serverjava.domain.concert.infrastructure;

import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concert.repository.ConcertReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class ConcertCoreReaderRepository implements ConcertReaderRepository {

    private final ConcertJPARepository concertJPARepository;
    @Override
    public Concert findConcert(Long concertId) {
        return concertJPARepository.findById(concertId)
                .orElseThrow(() -> new BaseException(NOT_FIND_CONCERT));
    }

    @Override
    public List<Concert> findConcertList(Concert.State state) {
        return concertJPARepository.findConcertsByStatus(state);
    }
}
