package hhplus.serverjava.api.user.usecase;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.CANNOT_REQUEST_SAMETIME;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.user.response.UserPointResponse;
import hhplus.serverjava.api.util.aop.DistributedLock;
import hhplus.serverjava.domain.user.componenets.UserPoint;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// @checkstyle:off
@Slf4j
@Service
@RequiredArgsConstructor
public class UserUsePointUseCaseV2 {

	private final UserPoint userPoint;

	// 포인트 사용
	@DistributedLock(key = "T(hhplus.serverjava.api.util.aop.LockType).USER_POINT.getKey(#userId)")
	public UserPointResponse use(Long userId, Long amount) {
		log.info("use 시작");
		try {
			User user = userPoint.pointUse(userId, amount);

			return new UserPointResponse(user.getId(), user.getPoint());
		} catch (ObjectOptimisticLockingFailureException e) {
			throw new BaseException(CANNOT_REQUEST_SAMETIME);
		}
	}
}
