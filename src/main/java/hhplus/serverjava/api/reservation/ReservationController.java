package hhplus.serverjava.api.reservation;

import hhplus.serverjava.api.concert.response.GetConcertResponse;
import hhplus.serverjava.api.concert.usecase.GetConcertListUseCase;
import hhplus.serverjava.api.reservation.request.PostReservationRequest;
import hhplus.serverjava.api.reservation.response.GetDateResponse;
import hhplus.serverjava.api.concert.usecase.FindAvailableSeatsUseCase;
import hhplus.serverjava.api.concert.usecase.FindConcertOptionUseCase;
import hhplus.serverjava.api.reservation.response.PostReservationResponse;
import hhplus.serverjava.api.reservation.usecase.MakeReservationUseCase;
import hhplus.serverjava.api.seat.response.GetSeatsResponse;
import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.support.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FIND_USER;


@Tag(name = "예약 Controller",
        description = "콘서트 조회 API, 예약 가능한 날짜 조회 API, 예약 가능한 좌석 조회 API, 콘서트 예약 API")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {

    private final GetConcertListUseCase getConcertListUseCase;
    private final FindConcertOptionUseCase findConcertOptionUseCase;
    private final FindAvailableSeatsUseCase findAvailableSeatsUseCase;
    private final MakeReservationUseCase makeReservationUseCase;

    /**
     * 콘서트 조회 API
     * [GET] /api/concert
     * @return BaseResponse<GetConcertResponse>
     */
    @Operation(summary = "콘서트 조회")
    @GetMapping("/concert")
    public BaseResponse<GetConcertResponse> getConcert() {

        GetConcertResponse execute = getConcertListUseCase.execute();

        return new BaseResponse<>(execute);
    }

    /**
     * 예약 가능한 날짜 조회 API
     * [GET] /api/concert/{concertId}/date
     * @return BaseResponse<GetDateResponse>
     */
    @Operation(summary = "예약 가능한 날짜 조회")
    @GetMapping("/concert/{concertId}/date")
    public BaseResponse<GetDateResponse> getAvailableDates(@PathVariable("concertId") Long concertId) {

        GetDateResponse execute = findConcertOptionUseCase.execute(concertId);

        return new BaseResponse<>(execute);
    }

    /**
     * 예약 가능한 좌석 조회 API
     * [GET] /api/concert/date/{date}/seats
     * @return BaseResponse<GetSeatsResponse>
     */
    @Operation(summary = "예약 가능한 좌석 조회")
    @GetMapping("/concert/date/{date}/seats")
    public BaseResponse<GetSeatsResponse> getAvailableSeats(@PathVariable("date") String targetDate, @RequestParam @NotNull Long concertId) {

        GetSeatsResponse execute = findAvailableSeatsUseCase.execute(concertId, targetDate);

        return new BaseResponse<>(execute);
    }

    /**
     * 콘서트 예약 API
     * [POST] /api/reservation
     * @return BaseResponse<PostReservationResponse>
     */
    @Operation(summary = "콘서트 예약")
    @PostMapping("/reservaton")
    public BaseResponse<PostReservationResponse> bookingConcert(HttpServletRequest request, @Valid @RequestBody PostReservationRequest reservationRequest) {

        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            throw new BaseException(NOT_FIND_USER);
        }

        PostReservationResponse execute = makeReservationUseCase.makeReservation(userId, reservationRequest);

        return new BaseResponse<>(execute);
    }
}
