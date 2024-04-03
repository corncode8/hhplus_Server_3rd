package hhplus.serverjava.domain.pointhistory.infrastructure;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointHistoryJPARepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findAllByUser_Id(Long userId);
}
