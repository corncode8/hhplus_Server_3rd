package hhplus.serverjava.api.user;

import hhplus.serverjava.api.user.request.PatchUserRequest;
import hhplus.serverjava.api.user.request.PostUserRequest;
import hhplus.serverjava.api.user.response.*;
import hhplus.serverjava.api.user.usecase.GetPointHistoryUseCase;
import hhplus.serverjava.api.user.usecase.GetTokenUseCase;
import hhplus.serverjava.api.user.usecase.GetUserPointUseCase;
import hhplus.serverjava.api.user.usecase.UserPointChargeUseCase;
import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.support.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.FAIL_FIND_QUEUE;

@Tag(name = "유저 Controller",
        description = "토큰 발급 API, 대기열 확인 API, 잔액 충전 API, 잔액 조회 API, 잔액 리스트 조회 API")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final GetTokenUseCase getTokenUseCase;
    private final GetPointHistoryUseCase getPointHistoryUseCase;
    private final GetUserPointUseCase getUserPointUseCase;
    private final UserPointChargeUseCase userPointChargeUseCase;

    /**
     * 토큰 발급 API
     * [GET] /api/wait
     * @return BaseResponse<GetTokenResponse>
     */
    @Operation(summary = "토큰 발급")
    @GetMapping("/wait")
    public BaseResponse<GetTokenResponse> getToken(@Valid @RequestBody PostUserRequest request) {

        // 유저 생성 + 토큰 발급
        GetTokenResponse execute = getTokenUseCase.execute(request.getUsername());

        return new BaseResponse<>(execute);
    }

    /**
     * 대기열 확인 API
     * [GET] /api/wait/check
     * @return BaseResponse<GetUserResponse>
     */
    @Operation(summary = "대기열 확인")
    @GetMapping("/wait/check")
    public BaseResponse<GetUserResponse> checkQueue(HttpServletRequest request) {

        // Interceptor에서 유저의 현재 대기열 정보 확인
        Long waitNum = (Long) request.getAttribute("waitNum");

        if (waitNum == null) {
            throw new BaseException(FAIL_FIND_QUEUE);
        }

        return new BaseResponse<>(new GetUserResponse(waitNum));
    }


    /**
     * 잔액 충전 API
     * [PATCH] /api/point/{userId}/charge
     * @return BaseResponse<UserPoint>
     */
    @Operation(summary = "잔액 충전")
    @PatchMapping("/point/{userId}/charge")
    public BaseResponse<UserPoint> chargePoint(@PathVariable("userId") Long userId, @Valid @RequestBody PatchUserRequest request) {

        UserPoint charge = userPointChargeUseCase.charge(userId, request.getAmount());

        return new BaseResponse<>(charge);
    }


    /**
     * 잔액 조회 API
     * [GET] /api/point/{userId}/account
     * @return BaseResponse<UserPoint>
     */
    @Operation(summary = "잔액 조회")
    @GetMapping("/point/{userId}/account")
    public BaseResponse<UserPoint> point(@PathVariable("userId")Long userId) {

        UserPoint execute = getUserPointUseCase.execute(userId);

        return new BaseResponse<>(execute);
    }


    /**
     * 잔액 리스트 조회 API
     * [GET] /api/point/{userId}/histories
     * @return BaseResponse<PointHistoryDto>
     */
    @Operation(summary = "잔액 리스트 조회")
    @GetMapping("/point/{userId}/histories")
    public BaseResponse<PointHistoryDto> pointHistory(@PathVariable("userId")Long userId) {

        PointHistoryDto execute = getPointHistoryUseCase.execute(userId);

        return new BaseResponse<>(execute);
    }
}
