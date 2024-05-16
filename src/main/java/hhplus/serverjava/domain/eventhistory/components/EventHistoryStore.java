package hhplus.serverjava.domain.eventhistory.components;

import org.springframework.stereotype.Component;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import hhplus.serverjava.domain.eventhistory.repository.EventHistoryStoreRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventHistoryStore {

	private final EventHistoryStoreRepository repository;

	public void publishEvent(EventHistory.Actor actor, Long actorId, EventHistory.eventChannel eventChannel) {
		EventHistory eventHistory = EventHistory.builder()
			.published(false)
			.actor(actor)
			.actorId(actorId)
			.eventChannel(eventChannel)
			.build();
		repository.save(eventHistory);
	}
}
