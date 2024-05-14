package hhplus.serverjava.api.user.usecase.v2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.user.request.GetWaitNumRequest;
import hhplus.serverjava.api.user.response.GetUserResponse;
import hhplus.serverjava.domain.queue.components.RedisQueueManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GetWaitNumUseCaseV2 {

	private final RedisQueueManager redisQueueManager;

	// 대기번호 return
	public GetUserResponse execute(GetWaitNumRequest request) {

		Long userRank = redisQueueManager.findUserRank(request.getConcertId(), request.getUserId());

		return new GetUserResponse(userRank);
	}

}
