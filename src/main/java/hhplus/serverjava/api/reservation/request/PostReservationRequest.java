package hhplus.serverjava.api.reservation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReservationRequest {

    private Long concertOptionId;

    // 콘서트 좌석
    private int seatNumber;

}
