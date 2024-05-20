package hhplus.serverjava.api.support.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import hhplus.serverjava.api.util.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

	private final JwtService jwtService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		Long userId = jwtService.getUserId();
		request.setAttribute("userId", userId);

		log.info("==== " + userId + "번 유저 Interceptor 진입 ====");

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
