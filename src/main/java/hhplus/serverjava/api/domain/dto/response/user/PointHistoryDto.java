package hhplus.serverjava.api.domain.dto.response.user;

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
}
