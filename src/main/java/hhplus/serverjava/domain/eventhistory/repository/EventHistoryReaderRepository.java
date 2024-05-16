package hhplus.serverjava.domain.eventhistory.repository;

import java.util.Optional;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;

public interface EventHistoryReaderRepository {

	Optional<EventHistory> findEventHistory(Long eventHistoryId);
}
