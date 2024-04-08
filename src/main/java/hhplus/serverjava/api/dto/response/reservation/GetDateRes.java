package hhplus.serverjava.api.dto.response.reservation;

import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class GetDateRes {

    List<LocalDateTime> availableDates;

    public GetDateRes (List<ConcertOption> concertOptionList) {
        this.availableDates = concertOptionList.stream()
                .map(concertOption -> concertOption.getConcertAt()).collect(Collectors.toList());
    }
}
