package hhplus.serverjava.domain.pointhistory.infrastructure;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import hhplus.serverjava.domain.pointhistory.repository.PointHistoryReaderRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PointHistoryCoreReaderRepository implements PointHistoryReaderRepository {

    private PointHistoryJPARepository pointHistoryJPARepository;


    @Override
    public List<PointHistory> readList(Long userId) {
        return pointHistoryJPARepository.findAllByUser_Id(userId);
    }

}
