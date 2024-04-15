package hhplus.serverjava.api.util.interceptor;

import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.api.util.jwt.JwtService;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;
    private final UserReader userReader;
    private final UserStore userStore;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long userId = jwtService.getUserId();

        log.info("==== " + userId +  "번 유저 Interceptor 진입 ====");

        try {
            request.setAttribute("userId", userId);

            User user = userReader.findUser(userId);

            // 대기열 로직
            if (user.getStatus().equals(User.State.WAITING)) {
                log.info("=== " + userId + "번 유저 대기열 진입 ===");

                // 현재 서비스를 이용중인 유저
                List<User> userList = userReader.findUsersByStatus(User.State.PROCESSING);

                // 대기번호 확인
                // PROCESSING 유저가 90명 미만일 경우 PROCESSING으로 변경
                Long userNum = userStore.getUserNum(user, userList);
                request.setAttribute("waitNum", userNum);
                log.info("=== " + userId + "번 유저 대기 " + userNum + "번" + " ===");
            }
            return true;
        } catch (BaseException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized : " + e.getMessage());
            return false;
        }
    }
}
