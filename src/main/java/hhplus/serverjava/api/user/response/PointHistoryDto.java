package hhplus.serverjava.api.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
public class PointHistoryDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PointHistoryList> pointHistoryList;

    public PointHistoryDto(List<PointHistory> pointHistories) {
        this.pointHistoryList = pointHistories.stream()
                .map(pointHistory -> new PointHistoryList(pointHistory))
                .collect(Collectors.toList());
    }

}
