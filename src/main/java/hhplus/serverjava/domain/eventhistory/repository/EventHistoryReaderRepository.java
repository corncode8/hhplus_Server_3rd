package hhplus.serverjava.domain.eventhistory.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;

public interface EventHistoryReaderRepository {

	Optional<EventHistory> findEventHistory(Long eventHistoryId);

	List<EventHistory> failEventList(LocalDateTime now);
}
