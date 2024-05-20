package hhplus.serverjava.domain.eventhistory.infrastructure;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;

public interface EventHistoryJpaRepository extends JpaRepository<EventHistory, Long> {

	@Query("select e from EventHistory e where e.createdAt < :now " + "and e.published = false")
	List<EventHistory> failEventList(@Param("now") LocalDateTime now);
}
