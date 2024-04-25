package hhplus.serverjava.domain.pointhistory.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {

	List<PointHistory> findAllByUser_Id(Long userId);
}
