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
    public GetConcertRes execute(Concert.State state) {

        List<Concert> concertList = concertReader.findConcertList(state);

        return new GetConcertRes(concertList);
    }
}
