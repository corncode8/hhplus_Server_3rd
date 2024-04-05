package hhplus.serverjava.domain.pointhistory.components;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import hhplus.serverjava.domain.pointhistory.repository.PointHistoryReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PointHistoryReader {

    private final PointHistoryReaderRepository pointHistoryReaderRepository;

    public List<PointHistory> readList(Long userId) {
        return pointHistoryReaderRepository.readList(userId);
    }
}
