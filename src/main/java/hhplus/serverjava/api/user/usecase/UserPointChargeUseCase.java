package hhplus.serverjava.api.user.usecase;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.user.response.UserPointResponse;
import hhplus.serverjava.domain.user.componenets.UserPoint;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.CANNOT_REQUEST_SAMETIME;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPointChargeUseCase {

    private final UserPoint userPoint;

    // 포인트 충전
    public UserPointResponse charge(Long userId, Long amount) {
        try {
            User user = userPoint.pointCharge(userId, amount);

            return new UserPointResponse(user.getId(), user.getPoint());
        } catch (OptimisticLockingFailureException e) {
            throw new BaseException(CANNOT_REQUEST_SAMETIME);
        }

    }
}
