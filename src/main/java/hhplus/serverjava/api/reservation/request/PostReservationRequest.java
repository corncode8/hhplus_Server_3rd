package hhplus.serverjava.api.reservation.request;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReservationRequest {

	@NotNull
	private Long concertOptionId;

	@NotNull
	private String targetDate;

	private int seatNum;

}
