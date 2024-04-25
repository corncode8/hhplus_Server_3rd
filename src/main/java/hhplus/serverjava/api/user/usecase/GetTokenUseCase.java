package hhplus.serverjava.api.user.usecase;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.user.response.GetTokenResponse;
import hhplus.serverjava.api.util.jwt.JwtService;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GetTokenUseCase {

	private final UserStore userStore;
	private final UserReader userReader;
	private final JwtService jwtService;

	public GetTokenResponse execute(String username) {
		Long point = 0L;

		try {
			// 유저 생성
			User user = User.builder()
				.name(username)
				.point(point)
				.updatedAt(LocalDateTime.now())
				.build();

			userStore.save(user);

			// 토큰 생성
			String jwt = jwtService.createJwt(user.getId());

			// 현재 서비스 이용중인 유저 List
			List<User> userUpdAscList = userReader.findUsersByStatus(User.State.PROCESSING);

			// 대기 번호 확인
			// 서비스를 이용중인 유저가 90명 미만이라면 바로 processing
			Long userWaitNum = userStore.getUserNum(user, userUpdAscList);

			return new GetTokenResponse(jwt, userWaitNum, user.getStatus());
		} catch (BaseException e) {
			throw new BaseException(FAIL_NEW_TOKEN);
		}
	}
}
