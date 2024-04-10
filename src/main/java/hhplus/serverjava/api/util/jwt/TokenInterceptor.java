package hhplus.serverjava.api.util.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("==== Interceptor 진입 ====");

        Long userId = jwtService.getUserId();
        if (jwtService.validToken(userId)) {

            log.info("==== 토큰 검증 성공! ====");
            return true;
        } else {
            log.info("==== 토큰 검증 실패 ====");
            return false;
        }
    }
}
