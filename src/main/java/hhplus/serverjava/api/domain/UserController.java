package hhplus.serverjava.api.domain;

import hhplus.serverjava.api.domain.dto.request.user.PostUserReq;
import hhplus.serverjava.api.domain.dto.response.user.GetUserRes;
import hhplus.serverjava.api.domain.dto.response.user.PointHistoryDto;
import hhplus.serverjava.api.domain.dto.response.user.PostUserRes;
import hhplus.serverjava.api.domain.dto.response.user.UserPoint;
import hhplus.serverjava.common.response.BaseResponse;
import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static hhplus.serverjava.common.response.BaseResponseStatus.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    /**
     * 토큰 발급 API
     * [POST] /api/getToken
     * @return BaseResponse<PostUserRes>
     */
    @PostMapping("")
    public BaseResponse<PostUserRes> getToken(@RequestBody PostUserReq postUserReq) {
        // postUserReq 유효성 검사

        if (postUserReq.getUsername().isEmpty()) {
            return new BaseResponse<>(USERS_EMPTY_NAME);
        }

        // 유저 생성 + 토큰 발급

        Long userId = 1L;
        Long listNum = 1L;
        LocalDateTime expectedAt = LocalDateTime.now();


        PostUserRes postUserRes = new PostUserRes(userId, listNum, expectedAt);

        return new BaseResponse<>(postUserRes);
    }

    /**
     * 대기열 확인 API
     * [GET] /api/check
     * @return BaseResponse<GetUserRes>
     */
    @GetMapping("/check")
    public BaseResponse<GetUserRes> checkQueue() {


        // 토큰으로 유저의 현재 대기열 정보 확인

        Long listNum = 1L;
        LocalDateTime expectedAt = LocalDateTime.now();
        GetUserRes getUserRes = new GetUserRes(listNum, expectedAt);

        return new BaseResponse<>(getUserRes);
    }


    /**
     * 잔액 충전 API
     * [POST] /api/{userId}/charge
     * @return BaseResponse<UserPoint>
     */
    @PostMapping("/{userId}/charge")
    public BaseResponse<UserPoint> chargePoint(@PathVariable("userId") Long userId, Long amount) {


        UserPoint userPoint = new UserPoint(userId, amount);

        return new BaseResponse<>(userPoint);
    }


    /**
     * 잔액 조회 API
     * [GET] /api/{userId}
     * @return BaseResponse<UserPoint>
     */
    @GetMapping("/{userId}")
    public BaseResponse<UserPoint> point(@PathVariable("userId")Long userId) {


        UserPoint userPoint = new UserPoint(userId, 65050L);

        return new BaseResponse<>(userPoint);
    }


    /**
     * 잔액 리스트 조회 API
     * [GET] /api/{userId}/histories
     * @return BaseResponse<List<PointHistoryDto>>
     */
    @GetMapping("/{userId}/histories")
    public BaseResponse<List<PointHistoryDto>> pointHistory(@PathVariable("userId")Long userId) {

        List<PointHistoryDto> pointHistoryDtoList = new ArrayList<>();

        Long id = 1L;
        PointHistory.State state = PointHistory.State.CHARGE;
        Long amount = 500L;
        LocalDateTime time = LocalDateTime.now();

        PointHistoryDto pointHistory = new PointHistoryDto(id, userId, state, amount, time);

        pointHistoryDtoList.add(pointHistory);

        return new BaseResponse<>(pointHistoryDtoList);
    }
}
