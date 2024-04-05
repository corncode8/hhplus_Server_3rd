package hhplus.serverjava.api.dto.response.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetReservationRes {

    private Long reservationId;

    private String concertName;
    private String concertArtist;
    private LocalDateTime concertAt;

    private int seatNum;
    private Long price;


}
