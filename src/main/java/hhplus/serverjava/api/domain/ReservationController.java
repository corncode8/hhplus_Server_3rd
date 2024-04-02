package hhplus.serverjava.api.domain;

import hhplus.serverjava.api.domain.dto.request.reservation.GetSeatsReq;
import hhplus.serverjava.api.domain.dto.request.reservation.PostReservationReq;
import hhplus.serverjava.api.domain.dto.response.reservation.GetDateRes;
import hhplus.serverjava.api.domain.dto.response.reservation.PostReservationRes;
import hhplus.serverjava.api.domain.dto.response.reservation.GetSeatsRes;
import hhplus.serverjava.common.response.BaseResponse;
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
public class ReservationController {

    /**
     * 예약 가능한 날짜 조회 API
     * [GET] /api/booking/{concertId}/list/time
     * @return BaseResponse<GetDateRes>
     */
    public BaseResponse<GetDateRes> getAvailableDates(@PathVariable("concertId") Long concertId) {


        // 예약 가능한 날짜 return
        LocalDateTime localDate = LocalDateTime.now().plusDays(5);
        LocalDateTime localDate1 = LocalDateTime.now().plusDays(6);
        LocalDateTime localDate2 = LocalDateTime.now().plusYears(90);


        List<LocalDateTime> localDateList = new ArrayList<>();

        localDateList.add(localDate);
        localDateList.add(localDate1);
        localDateList.add(localDate2);

        GetDateRes getDateRes = new GetDateRes(localDateList);

        return new BaseResponse<>(getDateRes);
    }

    /**
     * 예약 가능한 좌석 정보 조회 API
     * [GET] /api/booking/{concertId}/list/seats
     * @return BaseResponse<GetSeatsRes>
     */
    public BaseResponse<GetSeatsRes> getAvailableSeats(@PathVariable("concertId") Long concertId,
                                                       @RequestBody GetSeatsReq getSeatsReq) {

        if (getSeatsReq.getConcertDate() == null) {
            return new BaseResponse<>(CONCERT_EMPTY_DATE);
        }

        // 예약 가능한 좌석 정보 return
        List<String> mockList = new ArrayList<>();

        mockList.add("2d");
        mockList.add("3d");
        mockList.add("4d");
        mockList.add("2c");
        mockList.add("5c");


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

        // 콘서트 예약

        String username = "Justin Mock";

        String concertName = "MAKTUB Concert";

        request.setConcertAt(LocalDateTime.now());
        request.setSeat("SSSSSSSSSSSSSSSSS");

        PostReservationRes result = new PostReservationRes(username, concertId, concertName,
                request.getConcertAt(), request.getSeat());

        return new BaseResponse<>(result);
    }

}
