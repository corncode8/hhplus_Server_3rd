package hhplus.serverjava.api.domain;

import hhplus.serverjava.api.domain.dto.request.reservation.PostReservationReq;
import hhplus.serverjava.api.domain.dto.response.reservation.GetDateRes;
import hhplus.serverjava.api.domain.dto.response.reservation.PostReservationRes;
import hhplus.serverjava.api.domain.dto.response.reservation.GetSeatsRes;
import hhplus.serverjava.api.domain.usecase.concert.FindAvailableSeats;
import hhplus.serverjava.api.domain.usecase.concert.FindConcertOptionUseCase;
import hhplus.serverjava.common.response.BaseResponse;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {

    private FindConcertOptionUseCase findConcertOptionUseCase;
    private FindAvailableSeats findAvailableSeats;

    /**
     * 예약 가능한 날짜 조회 API
     * [GET] /api/booking/{concertId}/list/time
     * @return BaseResponse<GetDateRes>
     */
    @GetMapping("/booking/{concertId}/list/time")
    public BaseResponse<GetDateRes> getAvailableDates(@PathVariable("concertId") Long concertId) {

        GetDateRes execute = findConcertOptionUseCase.execute(concertId);

        return new BaseResponse<>(execute);
    }

    /**
     * 예약 가능한 좌석 정보 조회 API
     * [GET] /api/booking/{concertId}/dates/{date}/list/seats
     * @return BaseResponse<GetSeatsRes>
     */
    @GetMapping("/booking/{concertId}/dates/{date}/list/seats")
    public BaseResponse<GetSeatsRes> getAvailableSeats(@PathVariable("concertId") Long concertId,
                                                       @PathVariable("date") String concertDate) {

        GetSeatsRes execute = findAvailableSeats.execute(concertId, concertDate);

        return new BaseResponse<>(execute);
    }

    /**
     * 좌석 예약 API
     * [POST] /api/booking/{concertId}/seat
     * @return BaseResponse<GetSeatsRes>
     */
    @PostMapping("/booking/{concertId}/seat")
    public BaseResponse<PostReservationRes> bookingConcert(@PathVariable("concertId") Long concertId,
                                                           @RequestBody PostReservationReq request) {
        // 좌석 예약

        String username = "Justin Mock";

        String concertName = "MAKTUB Concert";


        PostReservationRes result = new PostReservationRes( concertName,
                request.getConcertAt(), request.getSeat());

        return new BaseResponse<>(result);
    }

}
