package hhplus.serverjava.domain.concert.components;

import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concert.repository.ConcertReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.EMPTY_CONCERT;
import static hhplus.serverjava.api.util.response.BaseResponseStatus.NOT_FIND_CONCERT;

@Component
@RequiredArgsConstructor
public class ConcertReader {

    private final ConcertReaderRepository concertReaderRepository;

    public Concert findConcert(Long concertId) {
        return concertReaderRepository.findConcert(concertId)
                .orElseThrow(() -> new BaseException(NOT_FIND_CONCERT));
    }

    public List<Concert> findConcertList(Concert.State state) {
        List<Concert> concertList = concertReaderRepository.findConcertList(state);

        if (concertList.isEmpty()) {
            throw new BaseException(EMPTY_CONCERT);
        }

        return concertReaderRepository.findConcertList(state);
    }
}
