package hhplus.serverjava.api.reservation;

import hhplus.serverjava.api.reservation.request.PostReservationRequest;
import hhplus.serverjava.api.reservation.response.GetReservationResponse;
import hhplus.serverjava.api.concert.usecase.FindAvailableSeatsUseCase;
import hhplus.serverjava.api.concert.usecase.FindConcertOptionUseCase;
import hhplus.serverjava.api.reservation.usecase.MakeReservationUseCase;
import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.api.util.jwt.JwtService;
import hhplus.serverjava.api.util.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.*;


@Tag(name = "예약 Controller",
        description = "콘서트 조회 API, 예약 가능한 날짜 조회 API, 예약 가능한 좌석 조회 API, 콘서트 예약 API")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {

    private final FindConcertOptionUseCase findConcertOptionUseCase;
    private final FindAvailableSeatsUseCase findAvailableSeatsUseCase;
    private final MakeReservationUseCase makeReservationUseCase;
    private final JwtService jwtService;

    /**
     * 콘서트 조회 API
     * [GET] /api/concert
     * @return BaseResponse<GetDateRes>
     */
    @Operation(summary = "콘서트 조회")
    @GetMapping("/concert")
    public BaseResponse<List<String>> getConcert() {

        String concertId = "1";
        String concertName = "MAKTUB CONCERT";
        String artist = "MAKTUB";

        List<String> execute = new ArrayList<>();
        execute.add(concertId);
        execute.add(concertName);
        execute.add(artist);

        return new BaseResponse<>(execute);
    }

    /**
     * 예약 가능한 날짜 조회 API
     * [GET] /api/concert/{concertId}/date
     * @return BaseResponse<GetDateRes>
     */
    @Operation(summary = "예약 가능한 날짜 조회")
    @GetMapping("/concert/{concertId}/date")
    public BaseResponse<List<String>> getAvailableDates(@PathVariable("concertId") Long concertId) {

        List<String> execute = new ArrayList<>();
        execute.add("2024-04-08");
        execute.add("2024-04-09");
        execute.add("2024-04-03");


        return new BaseResponse<>(execute);
    }

    /**
     * 예약 가능한 좌석 조회 API
     * [GET] /api/concert/date/{date}/seats
     * @return BaseResponse<GetSeatsRes>
     */
    @Operation(summary = "예약 가능한 좌석 조회")
    @GetMapping("/concert/date/{date}/seats")
    public BaseResponse<List<String>> getAvailableSeats(@PathVariable("date") String concertDate) {

        List<String> execute = new ArrayList<>();
        execute.add("1");
        execute.add("2");
        execute.add("5");
        execute.add("9");
        execute.add("16");

        return new BaseResponse<>(execute);
    }

    /**
     * 콘서트 예약 API
     * [POST] /api/reservation
     * @return BaseResponse<GetSeatsRes>
     */
    @Operation(summary = "콘서트 예약")
    @PostMapping("/reservaton")
    public BaseResponse<GetReservationResponse> bookingConcert(@RequestBody PostReservationRequest request) {

        GetReservationResponse execute = new GetReservationResponse(1L, "MAKTUB CONCERT", "MAKTUB",
                LocalDateTime.now().plusDays(1), 5, 50000L);


        return new BaseResponse<>(execute);
    }
}
