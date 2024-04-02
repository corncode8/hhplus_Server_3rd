package hhplus.serverjava.api.domain.dto.request.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReservationReq {

    // 콘서트 날짜 시간
    private LocalDateTime concertAt;

    // 콘서트 좌석
    private String seat;
}
