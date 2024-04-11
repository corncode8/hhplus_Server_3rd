package hhplus.serverjava.api.user.usecase;

import hhplus.serverjava.api.user.response.PointHistoryDto;
import hhplus.serverjava.domain.pointhistory.components.PointHistoryReader;
import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GetPointHistoryUseCase {

    private final PointHistoryReader pointHistoryReader;

    public List<PointHistoryDto> execute(Long userId) {
        List<PointHistory> pointHistories = pointHistoryReader.readList(userId);

        List<PointHistoryDto> pointHistoryDtos = pointHistories.stream()
                .map(ph -> new PointHistoryDto(
                        ph.getId(),
                        ph.getUser().getId(),
                        ph.getType(),
                        ph.getAmount(),
                        ph.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return pointHistoryDtos;
    }

}
