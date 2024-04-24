package hhplus.serverjava.api.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponse {

	// 대기 번호
	private Long listNum;

	// 예상 시간
	//    private LocalDateTime expectedTime;

}
