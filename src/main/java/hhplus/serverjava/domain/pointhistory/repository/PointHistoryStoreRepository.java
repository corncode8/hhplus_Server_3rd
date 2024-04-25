package hhplus.serverjava.domain.pointhistory.repository;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;

public interface PointHistoryStoreRepository {

	PointHistory save(PointHistory pointHistory);
}
