package hhplus.serverjava.domain.eventhistory.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;

public interface EventHistoryJPARepository extends JpaRepository<EventHistory, Long> {
}
