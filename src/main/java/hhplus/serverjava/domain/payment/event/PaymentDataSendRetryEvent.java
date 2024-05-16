package hhplus.serverjava.domain.payment.event;

import lombok.Getter;

@Getter
public class PaymentDataSendRetryEvent {

	private final Long eventId;
	private final Long paymentId;

	public PaymentDataSendRetryEvent(Long eventId, Long paymentId) {
		this.eventId = eventId;
		this.paymentId = paymentId;
	}
}
