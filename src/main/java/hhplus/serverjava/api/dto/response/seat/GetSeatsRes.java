package hhplus.serverjava.api.dto.response.seat;

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
public class GetSeatsRes {

    private Long concertOptionId;
    private List<Integer> seatList = new ArrayList<>();

    public GetSeatsRes(Long concertOptionId, List<Seat> seatList) {
        this.concertOptionId = concertOptionId;
        this.seatList =  seatList.stream()
                .map(seat -> seat.getSeatNum()).collect(Collectors.toList());
    }
}
