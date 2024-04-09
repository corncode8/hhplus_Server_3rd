package hhplus.serverjava.domain.concertoption.components;

import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.repository.ConcertOptionStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConcertOptionStore {

    private final ConcertOptionStoreRepository concertOptionStoreRepository;

    public ConcertOption save(ConcertOption concertOption) {
        return concertOptionStoreRepository.save(concertOption);
    }
}
