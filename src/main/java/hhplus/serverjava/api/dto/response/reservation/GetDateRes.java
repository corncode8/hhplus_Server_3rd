package hhplus.serverjava.api.dto.response.reservation;

import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class GetDateRes {

    List<String> availableDates;

    public GetDateRes (List<ConcertOption> concertOptionList) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.availableDates = concertOptionList.stream()
                .map(concertOption -> concertOption.getConcertAt().format(formatter)).collect(Collectors.toList());
    }
}
