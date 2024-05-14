package hhplus.serverjava.api.user.usecase.v2;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.CANNOT_REQUEST_SAMETIME;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.user.response.UserPointResponse;
import hhplus.serverjava.api.util.aop.DistributedLock;
import hhplus.serverjava.domain.user.componenets.UserPoint;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPointChargeUseCaseV2 {

	private final UserPoint userPoint;

	// 포인트 충전 (분산락 적용)
	@DistributedLock(key = "T(hhplus.serverjava.api.util.aop.LockType).USER_POINT.getKey(#userId)")
	public UserPointResponse charge(Long userId, Long amount) {
		log.info("Charge 시작");
		try {
			User user = userPoint.pointCharge(userId, amount);

			return new UserPointResponse(user.getId(), user.getPoint());
		} catch (OptimisticLockingFailureException e) {
			throw new BaseException(CANNOT_REQUEST_SAMETIME);
		}
	}
}
