package hhplus.serverjava.api.user.usecase;

import hhplus.serverjava.api.user.response.GetTokenResponse;
import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.api.util.jwt.JwtService;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.FAIL_NEW_TOKEN;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateTokenUseCase {

    private final UserStore userStore;
    private final UserReader userReader;
    private final JwtService jwtService;

    public GetTokenResponse execute(String username) {
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

            // 현재 서비스 이용중인 유저 List
            List<User> userUpdAscList = userReader.findUsersByStatus(User.State.PROCESSING);

            // 대기 번호 확인
            // 서비스를 이용중인 유저가 90명 미만이라면 바로 processing
            Long userWaitNum = userStore.getUserNum(user, userUpdAscList);

            userStore.save(user);

            return new GetTokenResponse(jwt, userWaitNum);
        } catch (BaseException e) {
            throw new BaseException(FAIL_NEW_TOKEN);
        }
    }
}
