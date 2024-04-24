package hhplus.serverjava.domain.pointhistory.infrastructure;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import hhplus.serverjava.domain.pointhistory.repository.PointHistoryStoreRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PointHistoryCoreStoreRepository implements PointHistoryStoreRepository {

	private final PointHistoryJPARepository pointHistoryJPARepository;

	public PointHistory save(PointHistory pointHistory) {
		return pointHistoryJPARepository.save(pointHistory);
	}
}
