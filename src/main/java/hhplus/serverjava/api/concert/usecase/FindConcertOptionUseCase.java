package hhplus.serverjava.api.concert.usecase;

import hhplus.serverjava.api.reservation.response.GetDateRes;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionReader;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class FindConcertOptionUseCase {

    private final ConcertOptionReader concertOptionReader;

    // 예약 가능 일자 API
    public GetDateRes execute(Long concertId) {

        // concertId로 콘서트 옵션 List
        List<ConcertOption> concertOption = concertOptionReader.findConcertOptionList(concertId);

        return new GetDateRes(concertOption);
    }

}
