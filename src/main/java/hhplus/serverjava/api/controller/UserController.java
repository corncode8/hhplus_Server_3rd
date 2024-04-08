package hhplus.serverjava.api.controller;

import hhplus.serverjava.api.dto.request.user.PostUserReq;
import hhplus.serverjava.api.dto.response.user.GetUserRes;
import hhplus.serverjava.api.dto.response.user.PointHistoryDto;
import hhplus.serverjava.api.dto.response.user.PostUserRes;
import hhplus.serverjava.api.dto.response.user.UserPoint;
import hhplus.serverjava.api.usecase.point.GetPointHistoryUseCase;
import hhplus.serverjava.api.usecase.point.GetUserPointUseCase;
import hhplus.serverjava.api.usecase.point.UserPointModifyUseCase;
import hhplus.serverjava.api.util.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static hhplus.serverjava.domain.pointhistory.entity.PointHistory.State.*;

@Tag(name = "유저 Controller",
        description = "토큰 발급 API, 대기열 확인 API, 잔액 충전 API, 잔액 조회 API, 잔액 리스트 조회 API")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final GetPointHistoryUseCase pointHistoryUseCase;
    private final GetUserPointUseCase getUserPointUseCase;
    private final UserPointModifyUseCase userPointModifyUseCase;

    /**
     * 토큰 발급 API
     * [GET] /api/wait
     * @return BaseResponse<PostUserRes>
     */
    @Operation(summary = "토큰 발급")
    @PostMapping("/wait")
    public BaseResponse<PostUserRes> getToken(@RequestBody PostUserReq postUserReq) {

        // 유저 생성 + 토큰 발급

        String token = "wer7w-edt-w5g-dsrgdrg-testToken";
        Long listNum = 1L;
        LocalDateTime expectedAt = LocalDateTime.now();


        PostUserRes postUserRes = new PostUserRes(token, listNum, expectedAt);

        return new BaseResponse<>(postUserRes);
    }

    /**
     * 대기열 확인 API
     * [GET] /api/wait/check
     * @return BaseResponse<GetUserRes>
     */
    @Operation(summary = "대기열 확인")
    @GetMapping("/wait/check")
    public BaseResponse<GetUserRes> checkQueue() {


        // 토큰으로 유저의 현재 대기열 정보 확인

        Long listNum = 1L;
        LocalDateTime expectedAt = LocalDateTime.now();
        GetUserRes getUserRes = new GetUserRes(listNum, expectedAt);

        return new BaseResponse<>(getUserRes);
    }


    /**
     * 잔액 충전 API
     * [PATCH] /api/point/{userId}/charge
     * @return BaseResponse<UserPoint>
     */
    @Operation(summary = "잔액 충전")
    @PatchMapping("/point/{userId}/charge")
    public BaseResponse<UserPoint> chargePoint(@PathVariable("userId") Long userId,@RequestBody Long amount) {

        UserPoint charge = new UserPoint(1L, 50000L);

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

        UserPoint userPoint = new UserPoint(1L, 65050L);

        return new BaseResponse<>(userPoint);
    }


    /**
     * 잔액 리스트 조회 API
     * [GET] /api/point/{userId}/histories
     * @return BaseResponse<List<PointHistoryDto>>
     */
    @Operation(summary = "잔액 리스트 조회")
    @GetMapping("/point/{userId}/histories")
    public BaseResponse<List<PointHistoryDto>> pointHistory(@PathVariable("userId")Long userId) {

        PointHistoryDto one = new PointHistoryDto(1L, 1L, CHARGE, 500L, LocalDateTime.now().minusDays(2));
        PointHistoryDto two = new PointHistoryDto(2L, 1L, USE, 100L, LocalDateTime.now().minusDays(1));

        List<PointHistoryDto> dtos = new ArrayList<>();
        dtos.add(one);
        dtos.add(two);

        return new BaseResponse<>(dtos);
    }
}
