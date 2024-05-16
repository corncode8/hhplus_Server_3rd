package hhplus.serverjava.domain.eventhistory.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import hhplus.serverjava.domain.eventhistory.repository.EventHistoryReaderRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EventHistoryCoreReaderRepository implements EventHistoryReaderRepository {

	private final EventHistoryJPARepository eventHistoryJPARepository;

	@Override
	public Optional<EventHistory> findEventHistory(Long eventHistoryId) {
		return eventHistoryJPARepository.findById(eventHistoryId);
	}
}
