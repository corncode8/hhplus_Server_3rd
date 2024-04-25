package hhplus.serverjava.domain.pointhistory.components;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.*;

import java.util.List;

import org.springframework.stereotype.Component;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import hhplus.serverjava.domain.pointhistory.repository.PointHistoryReaderRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointHistoryReader {
	private final PointHistoryReaderRepository pointHistoryReaderRepository;

	public List<PointHistory> readList(Long userId) {
		List<PointHistory> pointHistories = pointHistoryReaderRepository.readList(userId);

		if (pointHistories.isEmpty()) {
			throw new BaseException(NOT_FIND_POINT_LIST);
		}

		return pointHistoryReaderRepository.readList(userId);
	}
}
