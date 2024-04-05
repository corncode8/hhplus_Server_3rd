package hhplus.serverjava.domain.concert.infrastructure;

import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concert.repository.ConcertReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class ConcertCoreReaderRepository implements ConcertReaderRepository {

    private ConcertJPARepository concertJPARepository;
    @Override
    public Concert findConcert(Long concertId) {
        return concertJPARepository.findById(concertId)
                .orElseThrow(() -> new BaseException(NOT_FIND_CONCERT));
    }
}
