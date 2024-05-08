package hhplus.serverjava.api.payment.request;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPayRequest {

	private Long reservationId;
	@NotNull
	private int payAmount;
	private Long concertId;
}
