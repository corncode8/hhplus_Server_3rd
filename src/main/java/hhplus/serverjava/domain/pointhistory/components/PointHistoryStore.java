package hhplus.serverjava.domain.pointhistory.components;

import org.springframework.stereotype.Component;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import hhplus.serverjava.domain.pointhistory.repository.PointHistoryStoreRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointHistoryStore {
	private final PointHistoryStoreRepository pointHistoryStoreRepository;

	public PointHistory save(PointHistory pointHistory) {
		return pointHistoryStoreRepository.save(pointHistory);
	}
}
