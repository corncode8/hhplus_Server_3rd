package hhplus.serverjava.api.dto.response.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetConcertRes {

    private Long concertId;
    private String concertName;
    private String artist;
}
