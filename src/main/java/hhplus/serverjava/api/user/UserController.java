package hhplus.serverjava.api.user;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hhplus.serverjava.api.support.response.BaseResponse;
import hhplus.serverjava.api.user.request.GetTokenRequest;
import hhplus.serverjava.api.user.request.GetWaitNumRequest;
import hhplus.serverjava.api.user.request.PatchUserRequest;
import hhplus.serverjava.api.user.response.GetTokenResponse;
import hhplus.serverjava.api.user.response.GetUserResponse;
import hhplus.serverjava.api.user.response.PointHistoryDto;
import hhplus.serverjava.api.user.response.UserPointResponse;
import hhplus.serverjava.api.user.usecase.GetPointHistoryUseCase;
import hhplus.serverjava.api.user.usecase.GetUserPointUseCase;
import hhplus.serverjava.api.user.usecase.UserPointChargeUseCase;
import hhplus.serverjava.api.user.usecase.v2.GetTokenUseCaseV2;
import hhplus.serverjava.api.user.usecase.v2.GetWaitNumUseCaseV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "유저 Controller",
	description = "토큰 발급 API, 대기열 확인 API, 잔액 충전 API, 잔액 조회 API, 잔액 리스트 조회 API")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

	private final GetTokenUseCaseV2 getTokenUseCase;
	private final GetWaitNumUseCaseV2 getWaitNumUseCase;
	private final GetPointHistoryUseCase getPointHistoryUseCase;
	private final GetUserPointUseCase getUserPointUseCase;
	private final UserPointChargeUseCase userPointChargeUseCase;

	/**
	 * 토큰 발급 API
	 * [GET] /api/token
	 * @return BaseResponse<GetTokenResponse>
	 */
	@Operation(summary = "토큰 발급")
	@GetMapping("/token")
	public BaseResponse<GetTokenResponse> getToken(GetTokenRequest request) {

		// 유저 생성 + 토큰 발급
		GetTokenResponse execute = getTokenUseCase.execute(request);

		return new BaseResponse<>(execute);
	}

	/**
	 * 대기열 확인 API
	 * [GET] /api/wait/check
	 * @return BaseResponse<GetUserResponse>
	 */
	@Operation(summary = "대기열 확인")
	@GetMapping("/wait/check")
	public BaseResponse<GetUserResponse> checkQueue(HttpServletRequest request, @RequestParam Long concertId) {

		// Interceptor에서 유저의 현재 대기열 정보 확인
		Long userId = (Long)request.getAttribute("userId");
		GetUserResponse execute = getWaitNumUseCase.execute(new GetWaitNumRequest(concertId, userId));

		return new BaseResponse<>(execute);
	}

	/**
	 * 잔액 충전 API
	 * [PATCH] /api/point/{userId}/charge
	 * @return BaseResponse<UserPoint>
	 */
	@Operation(summary = "잔액 충전")
	@PatchMapping("/point/{userId}/charge")
	public BaseResponse<UserPointResponse> chargePoint(@PathVariable("userId") Long userId,
		@Valid @RequestBody PatchUserRequest request) {

		UserPointResponse charge = userPointChargeUseCase.charge(userId, request.getAmount());

		return new BaseResponse<>(charge);
	}

	/**
	 * 잔액 조회 API
	 * [GET] /api/point/{userId}/account
	 * @return BaseResponse<UserPoint>
	 */
	@Operation(summary = "잔액 조회")
	@GetMapping("/point/{userId}/account")
	public BaseResponse<UserPointResponse> point(@PathVariable("userId") Long userId) {

		UserPointResponse execute = getUserPointUseCase.execute(userId);

		return new BaseResponse<>(execute);
	}

	/**
	 * 잔액 리스트 조회 API
	 * [GET] /api/point/{userId}/histories
	 * @return BaseResponse<PointHistoryDto>
	 */
	@Operation(summary = "잔액 리스트 조회")
	@GetMapping("/point/{userId}/histories")
	public BaseResponse<PointHistoryDto> pointHistory(@PathVariable("userId") Long userId) {

		PointHistoryDto execute = getPointHistoryUseCase.execute(userId);

		return new BaseResponse<>(execute);
	}
}
