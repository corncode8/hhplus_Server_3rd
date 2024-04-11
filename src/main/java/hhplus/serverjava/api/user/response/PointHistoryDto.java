package hhplus.serverjava.api.user.response;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static hhplus.serverjava.domain.pointhistory.entity.PointHistory.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointHistoryDto {

    private Long id;
    private Long userId;
    private State state;
    private Long amount;
    private LocalDateTime time;

    public PointHistory toEntity() {
        return PointHistory.builder()
                .id(this.id)
                .amount(this.id)
                .type(this.state)
                .build();
    }
}
