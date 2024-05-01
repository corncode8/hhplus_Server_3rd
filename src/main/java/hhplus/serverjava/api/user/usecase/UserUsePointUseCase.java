package hhplus.serverjava.api.user.usecase;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.CANNOT_REQUEST_SAMETIME;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.user.response.UserPointResponse;
import hhplus.serverjava.domain.user.componenets.UserPoint;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

// @checkstyle:off
@Service
@Transactional
@RequiredArgsConstructor
public class UserUsePointUseCase {

	private final UserPoint userPoint;

	// 포인트 사용
	public UserPointResponse use(Long userId, Long amount) {
		try {
			User user = userPoint.pointUse(userId, amount);

			return new UserPointResponse(user.getId(), user.getPoint());
		} catch (ObjectOptimisticLockingFailureException e) {
			throw new BaseException(CANNOT_REQUEST_SAMETIME);
		}
	}
}
