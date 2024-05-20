package hhplus.serverjava.domain.payment.event;

import lombok.Getter;

@Getter
public class DataSendEvent {
	private final Long eventId;
	private final Long paymentId;

	public DataSendEvent(Long eventId, Long paymentId) {
		this.eventId = eventId;
		this.paymentId = paymentId;
	}
}
