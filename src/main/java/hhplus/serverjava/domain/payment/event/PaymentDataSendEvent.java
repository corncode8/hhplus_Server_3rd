package hhplus.serverjava.domain.payment.event;

import lombok.Getter;

@Getter
public class PaymentDataSendEvent {
	private final Long eventId;
	private final Long paymentId;

	public PaymentDataSendEvent(Long eventId, Long paymentId) {
		this.eventId = eventId;
		this.paymentId = paymentId;
	}
}
