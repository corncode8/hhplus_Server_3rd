package hhplus.serverjava.api.controller;

import hhplus.serverjava.api.dto.request.reservation.PostReservationReq;
import hhplus.serverjava.api.dto.response.reservation.GetConcertRes;
import hhplus.serverjava.api.dto.response.reservation.GetReservationRes;
import hhplus.serverjava.api.usecase.concert.FindAvailableSeatsUseCase;
import hhplus.serverjava.api.usecase.concert.FindConcertOptionUseCase;
import hhplus.serverjava.api.usecase.jwt.IsValidatedTokenUseCase;
import hhplus.serverjava.api.usecase.reservation.MakeReservationUseCase;
import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.api.util.jwt.JwtService;
import hhplus.serverjava.api.util.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.*;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {

    private FindConcertOptionUseCase findConcertOptionUseCase;
    private FindAvailableSeatsUseCase findAvailableSeatsUseCase;
    private MakeReservationUseCase makeReservationUseCase;
    private IsValidatedTokenUseCase isValidatedTokenUseCase;
    private JwtService jwtService;

    /**
     * 콘서트 조회 API
     * [GET] /api/concert
     * @return BaseResponse<GetDateRes>
     */
    @GetMapping("/concert")
    public BaseResponse<List<GetConcertRes>> getConcert() {

        // 토큰 검증
//        isValidatedTokenUseCase.execute(jwtService.getUserId());

        Long concertId = 1L;
        String concertName = "MAKTUB CONCERT";
        String artist = "MAKTUB";
        GetConcertRes concertInfo = new GetConcertRes(concertId, concertName, artist);

        List<GetConcertRes> execute = new ArrayList<>();
        execute.add(concertInfo);

        return new BaseResponse<>(execute);
    }

    /**
     * 예약 가능한 날짜 조회 API
     * [GET] /api/concert/{concertId}/date
     * @return BaseResponse<GetDateRes>
     */
    @GetMapping("/concert/{concertId}/date")
    public BaseResponse<List<String>> getAvailableDates(@PathVariable("concertId") Long concertId) {

        // 토큰 검증
//        isValidatedTokenUseCase.execute(jwtService.getUserId());

        List<String> execute = new ArrayList<>();
        execute.add("2024-04-08");
        execute.add("2024-04-09");
        execute.add("2024-04-03");


        return new BaseResponse<>(execute);
    }

    /**
     * 예약 가능한 좌석 정보 조회 API
     * [GET] /api/concert/date/{date}/list/seats
     * @return BaseResponse<GetSeatsRes>
     */
    @GetMapping("/concert/date/{date}/list/seats")
    public BaseResponse<List<String>> getAvailableSeats(@PathVariable("date") String concertDate) {

        // 토큰 검증
//        isValidatedTokenUseCase.execute(jwtService.getUserId());

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
    @PostMapping("/reservaton")
    public BaseResponse<GetReservationRes> bookingConcert(@RequestBody PostReservationReq request) {


        // 토큰 검증
//        User user = isValidatedTokenUseCase.execute(jwtService.getUserId());

        GetReservationRes execute = new GetReservationRes(1L, "MAKTUB CONCERT", "MAKTUB",
                LocalDateTime.now().plusDays(1), 5, 50000L);


        return new BaseResponse<>(execute);
    }



    private LocalDate dateParser(String StrDate) {
        try {
            return LocalDate.parse(StrDate, DateTimeFormatter.ISO_DATE);
        } catch (BaseException e) {
            throw new BaseException(INVALID_DATE);
        }

    }

}
