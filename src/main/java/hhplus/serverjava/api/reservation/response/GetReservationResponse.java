package hhplus.serverjava.api.reservation.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetReservationResponse {

	private Long reservationId;

	private String concertName;
	private String concertArtist;
	private LocalDateTime concertAt;

	private int seatNum;
	private Long price;

}
