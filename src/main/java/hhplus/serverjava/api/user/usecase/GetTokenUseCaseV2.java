package hhplus.serverjava.api.user.usecase;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.FAIL_NEW_TOKEN;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.user.request.GetTokenRequest;
import hhplus.serverjava.api.user.response.GetTokenResponse;
import hhplus.serverjava.api.util.jwt.JwtService;
import hhplus.serverjava.domain.queue.components.RedisQueueManager;
import hhplus.serverjava.domain.user.componenets.UserCreator;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GetTokenUseCaseV2 {

	private final UserStore userStore;
	private final JwtService jwtService;
	private final RedisQueueManager redisQueueManager;

	public GetTokenResponse execute(GetTokenRequest request) {
		Long point = 0L;

		try {
			// 유저 생성
			User user = UserCreator.create(request.getUsername(), point, LocalDateTime.now());
			userStore.save(user);

			// 토큰 생성
			String jwt = jwtService.createJwt(user.getId());

			Long userRank = redisQueueManager.addQueue(request.getConcertId(), user.getId());

			return new GetTokenResponse(jwt, userRank, user.getStatus());
		} catch (BaseException e) {
			throw new BaseException(FAIL_NEW_TOKEN);
		}
	}
}
