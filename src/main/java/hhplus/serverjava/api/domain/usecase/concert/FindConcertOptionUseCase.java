package hhplus.serverjava.api.domain.usecase.concert;

import hhplus.serverjava.api.domain.dto.response.reservation.GetDateRes;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionReader;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Transactional
@RequiredArgsConstructor
public class FindConcertOptionUseCase {

    private ConcertOptionReader concertOptionReader;

    public GetDateRes execute(Long concertId) {
        ConcertOption concertOption = concertOptionReader.findConcertOption(concertId);

        GetDateRes getDateRes = new GetDateRes();

        // 예약 가능 일자
        // ChronoUnit.DAYS.between(temp, target) : (일) 차이를 구함
        List<LocalDate> availableDates = Stream.iterate(
                        concertOption.getStartedAt(),
                        date -> date.plusDays(1)
                ).limit(ChronoUnit.DAYS.between(concertOption.getStartedAt(), concertOption.getEndedAt()))
                .collect(Collectors.toList());

        getDateRes.setAvailableDates(availableDates);
        getDateRes.setSeatNum(concertOption.getSeatsNum());

        return getDateRes;
    }
}
