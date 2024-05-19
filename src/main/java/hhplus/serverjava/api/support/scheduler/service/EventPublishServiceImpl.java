package hhplus.serverjava.api.support.scheduler.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import hhplus.serverjava.domain.eventhistory.components.EventHistoryReader;
import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import hhplus.serverjava.domain.payment.event.DataSendEvent;
import hhplus.serverjava.domain.payment.event.PaymentEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublishServiceImpl implements EventPublishService {

	private final PaymentEventPublisher eventPublisher;
	private final EventHistoryReader eventHistoryReader;

	@Override
	public void publishDataSendEvent() {
		LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
		List<EventHistory> eventHistories = eventHistoryReader.failEventList(fiveMinutesAgo);

		for (EventHistory eventHistory : eventHistories) {
			eventPublisher.eventPublish(
				new DataSendEvent(eventHistory.getId(), eventHistory.getActorId()));
		}
	}
}
