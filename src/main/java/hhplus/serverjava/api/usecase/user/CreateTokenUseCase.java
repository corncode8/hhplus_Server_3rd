package hhplus.serverjava.api.usecase.user;

import hhplus.serverjava.api.dto.response.user.GetTokenRes;
import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.api.util.jwt.JwtService;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateTokenUseCase {

    private final UserStore userStore;
    private final UserReader userReader;
    private final JwtService jwtService;

    public GetTokenRes execute(String username) {
        Long point = 0L;

        try {
            // 유저 생성
            User user = User.builder()
                    .name(username)
                    .point(point)
                    .updatedAt(LocalDateTime.now())
                    .build();

            // 토큰 생성
            String jwt = jwtService.createJwt(user.getId());

            // 대기 번호 확인
            Long userWaitNum = getUserNum(user.getId());

            // 서비스를 이용중인 유저가 50명 이하일 경우
            if (userWaitNum <= 50) {
                user.setProcessing();
            }

            userStore.save(user);

            return new GetTokenRes(jwt, userWaitNum);

        } catch (BaseException e) {
            throw new BaseException(FAIL_NEW_TOKEN);
        }

    }

    private Long getUserNum(Long userId) {
        List<User> userUpdAscList = getWaitUserList();
        Collections.sort(userUpdAscList, Comparator.comparing(user -> user.getUpdatedAt()));

        User recentlyUpdUser = userUpdAscList.get(userUpdAscList.size() -1);
        Long userNum = userId - recentlyUpdUser.getId();

        return userNum;
    }

    private List<User> getWaitUserList() {
        return userReader.findUsersByStatus(User.State.PROCESSING);
    }
}
