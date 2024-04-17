package hhplus.serverjava.api.payment;

import hhplus.serverjava.api.payment.request.PostPayRequest;
import hhplus.serverjava.api.payment.response.PostPayResponse;
import hhplus.serverjava.api.payment.usecase.PaymentUseCase;
import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.support.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FIND_USER;

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
    public BaseResponse<PostPayResponse> payment(HttpServletRequest request, @Valid @RequestBody PostPayRequest postRequest) {

        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            throw new BaseException(NOT_FIND_USER);
        }

        PostPayResponse execute = paymentUseCase.execute(postRequest, userId);

        return new BaseResponse<>(execute);
    }
}

