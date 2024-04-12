package hhplus.serverjava.api.user.usecase;

import hhplus.serverjava.api.user.response.GetUserResponse;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GetWaitNumUseCase {

    private final UserReader userReader;
    private final UserStore userStore;

    public GetUserResponse execute(Long userId) {

        User user = userReader.findUser(userId);

        // 현재 서비스를 이용중인 유저
        List<User> userList = userReader.findUsersByStatus(User.State.PROCESSING);

        // 대기번호 확인
        // PROCESSING 유저가 90명 미만일 경우 PROCESSING으로 변경
        Long userNum = userStore.getUserNum(user, userList);

        return new GetUserResponse(userNum);
    }

}
