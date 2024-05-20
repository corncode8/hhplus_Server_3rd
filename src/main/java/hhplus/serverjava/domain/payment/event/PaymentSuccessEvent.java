package hhplus.serverjava.domain.payment.event;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import hhplus.serverjava.domain.payment.entity.Payment;
import lombok.Getter;

@Getter
public class PaymentSuccessEvent {

	private final Payment payment;
	private EventHistory.Actor actor = EventHistory.Actor.PAY;

	public PaymentSuccessEvent(Payment payment) {
		this.payment = payment;
	}
}
