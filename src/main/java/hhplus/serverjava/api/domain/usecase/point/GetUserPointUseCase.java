package hhplus.serverjava.api.domain.usecase.point;

import hhplus.serverjava.api.domain.dto.response.user.UserPoint;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class GetUserPointUseCase {

    private UserReader userReader;

    public UserPoint execute(Long userId) {
        User user = userReader.findUser(userId);
        return new UserPoint(user.getId(), user.getPoint());
    }
}
