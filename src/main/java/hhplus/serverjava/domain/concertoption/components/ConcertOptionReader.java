package hhplus.serverjava.domain.concertoption.components;

import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.repository.ConcertOptionReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.NOT_FOUND_CONCERT_OPTION;

@Component
@RequiredArgsConstructor
public class ConcertOptionReader {

    private final ConcertOptionReaderRepository concertOptionReaderRepository;

    public List<ConcertOption> findConcertOptionList(Long concertId) {
        List<ConcertOption> concertOptionList = concertOptionReaderRepository.findConcertOptionList(concertId);

        if (concertOptionList.isEmpty()) {
            throw new BaseException(NOT_FOUND_CONCERT_OPTION);
        }

        return concertOptionReaderRepository.findConcertOptionList(concertId);
    }

    public ConcertOption findConcertOption(Long concertId, LocalDateTime concertAt) {
        return concertOptionReaderRepository.findConcertOption(concertId, concertAt)
                .orElseThrow(() -> new BaseException(NOT_FOUND_CONCERT_OPTION));
    }

    public Concert findConcert(Long concertOptionId) {
        return concertOptionReaderRepository.findConcert(concertOptionId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_CONCERT_OPTION));
    }
}
