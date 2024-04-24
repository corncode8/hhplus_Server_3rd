package hhplus.serverjava.domain.pointhistory.repository;

import java.util.List;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;

public interface PointHistoryReaderRepository {
	List<PointHistory> readList(Long userId);
}
