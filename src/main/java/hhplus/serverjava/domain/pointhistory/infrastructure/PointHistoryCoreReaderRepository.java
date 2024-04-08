package hhplus.serverjava.domain.pointhistory.infrastructure;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import hhplus.serverjava.domain.pointhistory.repository.PointHistoryReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PointHistoryCoreReaderRepository implements PointHistoryReaderRepository {

    private final PointHistoryJPARepository pointHistoryJPARepository;


    @Override
    public List<PointHistory> readList(Long userId) {
        return pointHistoryJPARepository.findAllByUser_Id(userId);
    }

}
