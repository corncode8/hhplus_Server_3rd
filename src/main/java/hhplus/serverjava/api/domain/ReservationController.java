package hhplus.serverjava.api.domain;

import hhplus.serverjava.api.domain.dto.request.reservation.PostReservationReq;
import hhplus.serverjava.api.domain.dto.response.reservation.GetDateRes;
import hhplus.serverjava.api.domain.dto.response.reservation.PostReservationRes;
import hhplus.serverjava.api.domain.dto.response.reservation.GetSeatsRes;
import hhplus.serverjava.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {

    /**
     * 예약 가능한 날짜 조회 API
     * [GET] /api/booking/{concertId}/list/time
     * @return BaseResponse<GetDateRes>
     */
    @GetMapping("/booking/{concertId}/list/time")
    public BaseResponse<GetDateRes> getAvailableDates(@PathVariable("concertId") Long concertId) {


        // 예약 가능한 날짜 return
        LocalDate localDate = LocalDate.now().plusDays(5);
        LocalDate localDate1 = LocalDate.now().plusDays(6);
        LocalDate localDate2 = LocalDate.now().plusYears(90);


        List<LocalDate> localDateList = new ArrayList<>();

        localDateList.add(localDate);
        localDateList.add(localDate1);
        localDateList.add(localDate2);

        GetDateRes getDateRes = new GetDateRes(localDateList);

        return new BaseResponse<>(getDateRes);
    }

    /**
     * 예약 가능한 좌석 정보 조회 API
     * [GET] /api/booking/{concertId}/dates/{date}/list/seats
     * @return BaseResponse<GetSeatsRes>
     */
    @GetMapping("/booking/{concertId}/dates/{date}/list/seats")
    public BaseResponse<GetSeatsRes> getAvailableSeats(@PathVariable("concertId") Long concertId,
                                                       @PathVariable("date") String concertDate) {

        // 예약 가능한 좌석 정보 return
        List<String> mockList = new ArrayList<>();

        mockList.add("1");
        mockList.add("2");
        mockList.add("5");
        mockList.add("9");
        mockList.add("16");


        GetSeatsRes getSeatsRes = new GetSeatsRes(mockList);

        return new BaseResponse<>(getSeatsRes);
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
