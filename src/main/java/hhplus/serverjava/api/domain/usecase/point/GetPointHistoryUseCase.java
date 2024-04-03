package hhplus.serverjava.api.domain.usecase.point;

import hhplus.serverjava.api.domain.dto.response.user.PointHistoryDto;
import hhplus.serverjava.domain.pointhistory.components.PointHistoryReader;
import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
public class GetPointHistoryUseCase {

    private PointHistoryReader pointHistoryReader;

    public List<PointHistoryDto> execute(Long userId) {
        List<PointHistory> pointHistories = pointHistoryReader.readList(userId);

        List<PointHistoryDto> pointHistoryDtos = pointHistories.stream()
                .map(ph -> new PointHistoryDto(
                        ph.getId(),
                        ph.getUser().getId(),
                        ph.getState(),
                        ph.getAmount(),
                        ph.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return pointHistoryDtos;
    }
}
