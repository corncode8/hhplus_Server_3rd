package hhplus.serverjava.api.usecase.user;

import hhplus.serverjava.api.dto.response.user.GetUserRes;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GetWaitNumUseCase {

    private final UserReader userReader;
    private final UserStore userStore;

    public GetUserRes execute(Long userId) {

        // 대기번호 확인
        List<User> userList = getWaitUserList();
        Long userNum = getUserNum(userList, userId);

        // PROCESSING 유저가 100명 미만일 경우 PROCESSING으로 변경
        if (setUserProcessing(userList)) {
            User user = findUser(userId);

            user.setProcessing();
            userStore.save(user);
        }

        return new GetUserRes(userNum);
    }

    private Long getUserNum(List<User> users, Long userId) {
        Collections.sort(users, Comparator.comparing(user -> user.getUpdatedAt()));

        User recentlyUpdUser = users.get(users.size() -1);
        Long userNum = userId - recentlyUpdUser.getId();

        return userNum;
    }

    private Boolean setUserProcessing(List<User> users) {

        // 서비스를 이용중인 유저가 100명보다 많다면 false
        if (users.size() > 100) {
            return false;
        }

        return true;
    }

    private List<User> getWaitUserList() {
        return userReader.findUsersByStatus(User.State.PROCESSING);
    }

    private User findUser(Long userId) {
        return userReader.findUser(userId);
    }
}