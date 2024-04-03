package hhplus.serverjava.domain.pointhistory.repository;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;

import java.util.List;

public interface PointHistoryReaderRepository {
    List<PointHistory> readList(Long userId);
}
