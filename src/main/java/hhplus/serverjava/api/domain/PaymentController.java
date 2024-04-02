package hhplus.serverjava.api.domain;

import hhplus.serverjava.api.domain.dto.response.payment.PostPayRes;
import hhplus.serverjava.common.response.BaseResponse;
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
     * [POST] /api/{reservationId}/payment
     * @return BaseResponse<UserPoint>
     */
    @PostMapping("/{reservationId}/payment")
    public BaseResponse<PostPayRes> point(@PathVariable("reservationId")Long reservationId, int payAmount) {

        Long payId = 1L;

        LocalDateTime payAt = LocalDateTime.now();

        PostPayRes postPayRes = new PostPayRes(payId,reservationId, payAmount, payAt);


        return new BaseResponse<>(postPayRes);
    }
}
