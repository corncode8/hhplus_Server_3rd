package hhplus.serverjava.api.domain.dto.request.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReservationReq {

    // 콘서트 날짜 시간
    private String concertAt;

    // 콘서트 좌석
    private String seat;
}
