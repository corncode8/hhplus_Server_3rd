package hhplus.serverjava.domain.eventhistory.infrastructure;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import hhplus.serverjava.domain.eventhistory.repository.EventHistoryReaderRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EventHistoryCoreReaderRepository implements EventHistoryReaderRepository {

	private final EventHistoryJpaRepository eventHistoryJpaRepository;

	@Override
	public Optional<EventHistory> findEventHistory(Long eventHistoryId) {
		return eventHistoryJpaRepository.findById(eventHistoryId);
	}

	@Override
	public List<EventHistory> failEventList(LocalDateTime now) {
		return eventHistoryJpaRepository.failEventList(now);
	}
}
