package hhplus.serverjava.api.util.config;

import hhplus.serverjava.api.util.jwt.JwtService;
import hhplus.serverjava.api.support.interceptor.TokenInterceptor;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtService jwtService;
    private final UserReader userReader;
    private final UserStore userStore;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(jwtService, userReader, userStore))
                .addPathPatterns("/api/wait/check", "/api/concert/**", "/api/reservation", "/api/payment");
        // 결제시 토큰 검증?
    }
}
