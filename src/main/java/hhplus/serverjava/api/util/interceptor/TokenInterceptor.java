package hhplus.serverjava.api.util.interceptor;

import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.api.util.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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

        try {
            Long userId = jwtService.getUserId();
            request.setAttribute("userId", userId);

            // 대기열 로직

            return true;
        } catch (BaseException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized : " + e.getMessage());
            return false;
        }
    }
}
