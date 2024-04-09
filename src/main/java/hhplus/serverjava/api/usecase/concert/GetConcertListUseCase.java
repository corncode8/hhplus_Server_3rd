package hhplus.serverjava.api.usecase.concert;

import hhplus.serverjava.api.dto.response.concert.GetConcertRes;
import hhplus.serverjava.domain.concert.components.ConcertReader;
import hhplus.serverjava.domain.concert.entity.Concert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetConcertListUseCase {

    private final ConcertReader concertReader;

    // 콘서트 조회 API
    public GetConcertRes execute() {

        // 현재 상영중인 콘서트 List
        List<Concert> concertList = getConcertList(Concert.State.SHOWING);

        return new GetConcertRes(concertList);
    }

    private List<Concert> getConcertList(Concert.State state) {
        return concertReader.findConcertList(state);
    }
}
