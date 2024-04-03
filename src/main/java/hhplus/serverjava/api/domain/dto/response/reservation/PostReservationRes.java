package hhplus.serverjava.api.domain.dto.response.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReservationRes {

    private String concertName;

    private String reservationDate;
    private String reservationSeat;

}
