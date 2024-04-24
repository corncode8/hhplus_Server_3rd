package hhplus.serverjava.api.user.response;

import hhplus.serverjava.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetTokenResponse {

	// 토큰
	private String token;

	// 대기 번호
	private Long listNum;

	// 유저 상태
	private User.State state;

	// 예상 시간
	//    private LocalDateTime expectedTime;
}
