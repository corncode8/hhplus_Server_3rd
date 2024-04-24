package hhplus.serverjava.domain.pointhistory.infrastructure;

import java.util.List;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import hhplus.serverjava.domain.pointhistory.repository.PointHistoryReaderRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PointHistoryCoreReaderRepository implements PointHistoryReaderRepository {

	private final PointHistoryJPARepository pointHistoryJPARepository;

	@Override
	public List<PointHistory> readList(Long userId) {
		return pointHistoryJPARepository.findAllByUser_Id(userId);
	}

}
