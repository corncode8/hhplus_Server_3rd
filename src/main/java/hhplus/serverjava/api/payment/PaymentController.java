package hhplus.serverjava.api.payment;

import hhplus.serverjava.api.payment.response.PostPayRes;
import hhplus.serverjava.api.util.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "결제 Controller", description = "결제 API")
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
    @Operation(summary = "결제")
    @PostMapping("/payment")
    public BaseResponse<PostPayRes> point(@PathVariable("reservationId")Long reservationId, @RequestBody int payAmount) {

        Long payId = 1L;

        LocalDateTime payAt = LocalDateTime.now();

        PostPayRes postPayRes = new PostPayRes(payId,reservationId, payAmount, payAt);


        return new BaseResponse<>(postPayRes);
    }
}

