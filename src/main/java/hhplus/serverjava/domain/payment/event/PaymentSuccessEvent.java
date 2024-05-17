package hhplus.serverjava.domain.payment.event;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import lombok.Getter;

@Getter
public class PaymentSuccessEvent {

	private final Long paymentId;
	private EventHistory.Actor actor = EventHistory.Actor.PAY;
	private EventHistory.eventChannel eventChannel = EventHistory.eventChannel.PAYMENT;

	public PaymentSuccessEvent(Long paymentId) {
		this.paymentId = paymentId;
	}
}
