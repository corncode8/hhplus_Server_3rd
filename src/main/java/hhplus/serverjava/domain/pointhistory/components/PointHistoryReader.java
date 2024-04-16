package hhplus.serverjava.domain.pointhistory.components;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import hhplus.serverjava.domain.pointhistory.repository.PointHistoryReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FIND_POINT_LIST;

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
