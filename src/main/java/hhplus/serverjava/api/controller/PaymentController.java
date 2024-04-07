package hhplus.serverjava.api.controller;

import hhplus.serverjava.api.dto.response.payment.PostPayRes;
import hhplus.serverjava.api.util.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    /**
     * 결제 API
     * [POST] /api/payment
     * @return BaseResponse<UserPoint>
     */
    @PostMapping("/payment")
    public BaseResponse<PostPayRes> point(@PathVariable("reservationId")Long reservationId, @RequestBody int payAmount) {

        Long payId = 1L;

        LocalDateTime payAt = LocalDateTime.now();

        PostPayRes postPayRes = new PostPayRes(payId,reservationId, payAmount, payAt);


        return new BaseResponse<>(postPayRes);
    }
}

