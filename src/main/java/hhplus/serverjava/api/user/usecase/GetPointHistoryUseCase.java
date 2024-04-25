package hhplus.serverjava.api.user.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.user.response.PointHistoryDto;
import hhplus.serverjava.domain.pointhistory.components.PointHistoryReader;
import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GetPointHistoryUseCase {

	private final PointHistoryReader pointHistoryReader;

	public PointHistoryDto execute(Long userId) {
		List<PointHistory> pointHistories = pointHistoryReader.readList(userId);

		return new PointHistoryDto(pointHistories);
	}

}
