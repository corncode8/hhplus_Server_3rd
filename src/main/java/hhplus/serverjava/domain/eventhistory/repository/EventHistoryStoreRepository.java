package hhplus.serverjava.domain.eventhistory.repository;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;

public interface EventHistoryStoreRepository {

	EventHistory save(EventHistory eventHistory);
}
