package hhplus.serverjava.api.dto.response.seat;

import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.AllArgsConstructor;
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

    private List<Integer> seatList = new ArrayList<>();

    public GetSeatsRes(List<Seat> seatList) {
        this.seatList =  seatList.stream()
                .map(seat -> seat.getSeatNum()).collect(Collectors.toList());
    }
}
