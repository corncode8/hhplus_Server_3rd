package hhplus.serverjava.api.reservation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReservationReq {

    private Long concertOptionId;

    // 콘서트 좌석
    private int seatNumber;

}
