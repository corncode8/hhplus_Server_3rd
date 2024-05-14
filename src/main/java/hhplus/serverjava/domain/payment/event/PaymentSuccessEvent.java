package hhplus.serverjava.domain.payment.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentSuccessEvent {
	private final Long reservationId;
	private final Long paymentId;
}
