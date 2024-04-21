package hhplus.serverjava.api.reservation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class GetDateResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<String> availableDates;

    public GetDateResponse(List<ConcertOption> concertOptionList) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.availableDates = concertOptionList.stream()
                .map(concertOption -> concertOption.getConcertAt().format(formatter)).collect(Collectors.toList());
    }
}
