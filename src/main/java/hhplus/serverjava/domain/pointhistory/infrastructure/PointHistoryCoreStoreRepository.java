package hhplus.serverjava.domain.pointhistory.infrastructure;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import hhplus.serverjava.domain.pointhistory.repository.PointHistoryStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointHistoryCoreStoreRepository implements PointHistoryStoreRepository {

    private final PointHistoryJPARepository pointHistoryJPARepository;

    public PointHistory save(PointHistory pointHistory) {
        return pointHistoryJPARepository.save(pointHistory);
    }
}
