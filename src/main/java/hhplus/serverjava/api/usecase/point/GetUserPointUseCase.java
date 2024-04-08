package hhplus.serverjava.api.usecase.point;

import hhplus.serverjava.api.dto.response.user.UserPoint;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GetUserPointUseCase {

    private final UserReader userReader;

    public UserPoint execute(Long userId) {
        User user = findUser(userId);
        return new UserPoint(user.getId(), user.getPoint());
    }

    private User findUser(Long userId) {
        return userReader.findUser(userId);
    }
}
