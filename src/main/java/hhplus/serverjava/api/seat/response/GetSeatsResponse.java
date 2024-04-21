package hhplus.serverjava.api.seat.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class GetSeatsResponse {

    private Long concertOptionId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Integer> seatList = new ArrayList<>();

    public GetSeatsResponse(Long concertOptionId, List<Seat> seatList) {
        this.concertOptionId = concertOptionId;
        this.seatList =  seatList.stream()
                .map(seat -> seat.getSeatNum()).collect(Collectors.toList());
    }
}
