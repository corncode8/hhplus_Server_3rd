package hhplus.serverjava.api.user.response;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static hhplus.serverjava.domain.pointhistory.entity.PointHistory.*;

@Getter
@Setter
@NoArgsConstructor
public class PointHistoryDto {

    private List<PointHistoryList> pointHistoryListList;

    public PointHistoryDto(List<PointHistory> pointHistories) {
        this.pointHistoryListList = pointHistories.stream()
                .map(pointHistory -> new PointHistoryList(pointHistory))
                .collect(Collectors.toList());
    }

}
