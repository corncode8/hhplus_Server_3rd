package hhplus.serverjava.domain.eventhistory.event;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import lombok.Getter;

@Getter
public class EventHistorySave {

	private final Long paymentId;
	private EventHistory.Actor actor = EventHistory.Actor.PAY;
	private EventHistory.eventChannel eventChannel = EventHistory.eventChannel.PAYMENT;

	public EventHistorySave(Long paymentId) {
		this.paymentId = paymentId;
	}
}
