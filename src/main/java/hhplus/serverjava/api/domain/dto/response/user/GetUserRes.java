package hhplus.serverjava.api.domain.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserRes {

    // 예상 대기 번호
    private Long listNum;

    // 예상 시간
    private LocalDateTime expectedTime;

}
