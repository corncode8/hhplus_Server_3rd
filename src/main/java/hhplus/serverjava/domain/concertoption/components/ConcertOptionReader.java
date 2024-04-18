package hhplus.serverjava.domain.concertoption.components;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.repository.ConcertOptionReaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FOUND_CONCERT_OPTION;

@Slf4j
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
        log.info("Searching for ConcertOption with concertId: {} and concertAt: {}", concertId, concertAt);
        return concertOptionReaderRepository.findConcertOption(concertId, concertAt)
                .orElseThrow(() -> {
                    log.error("No ConcertOption found for concertId: {} and concertAt: {}", concertId, concertAt);
                    return new BaseException(NOT_FOUND_CONCERT_OPTION);
                });
    }

    public Concert findConcert(Long concertOptionId) {
        return concertOptionReaderRepository.findConcert(concertOptionId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_CONCERT_OPTION));
    }
}
