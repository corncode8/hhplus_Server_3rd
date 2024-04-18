package hhplus.serverjava.domain.pointhistory.repository;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;

import java.util.Optional;

public interface PointHistoryStoreRepository {

    PointHistory save(PointHistory pointHistory);
}
