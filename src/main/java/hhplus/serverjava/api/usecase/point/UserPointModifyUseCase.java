package hhplus.serverjava.api.usecase.point;

import hhplus.serverjava.api.dto.response.user.UserPoint;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPointModifyUseCase {

    private final UserStore userStore;

    // 포인트 충전
    public UserPoint charge(Long userId, Long amount) {
        User user = userStore.findUser(userId);
        user.setPoint(user.getPoint() + amount);

        User modify = userStore.modify(user);

        return new UserPoint(modify.getId(), modify.getPoint());
    }
}
