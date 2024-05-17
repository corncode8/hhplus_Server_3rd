package hhplus.serverjava.domain.eventhistory.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import hhplus.serverjava.domain.eventhistory.repository.EventHistoryStoreRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EventHistoryCoreStoreRepository implements EventHistoryStoreRepository {

	private final EventHistoryJPARepository eventHistoryJPARepository;

	@Override
	public EventHistory save(EventHistory eventHistory) {
		return eventHistoryJPARepository.save(eventHistory);
	}

	@Override
	public Optional<EventHistory> findEventHistory(Long eventHistoryId) {
		return eventHistoryJPARepository.findById(eventHistoryId);
	}
}
