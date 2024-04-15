package hhplus.serverjava.api.payment;

import hhplus.serverjava.api.payment.response.PostPayResponse;
import hhplus.serverjava.api.payment.usecase.PaymentUseCase;
import hhplus.serverjava.api.util.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "결제 Controller", description = "결제 API")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentUseCase paymentUseCase;

    /**
     * 결제 API
     * [POST] /api/payment
     * @return BaseResponse<PostPayResponse>
     */
    @Operation(summary = "결제")
    @PostMapping("/payment")
    public BaseResponse<PostPayResponse> point(@PathVariable("reservationId")Long reservationId, @RequestBody int payAmount) {

        PostPayResponse execute = paymentUseCase.execute(reservationId, payAmount);

        return new BaseResponse<>(execute);
    }
}

