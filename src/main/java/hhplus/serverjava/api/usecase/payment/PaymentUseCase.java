package hhplus.serverjava.api.usecase.payment;

import hhplus.serverjava.api.dto.response.payment.PostPayRes;
import hhplus.serverjava.domain.payment.components.PaymentStore;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Server
@Transactional
@RequiredArgsConstructor
public class PaymentUseCase {

    private final PaymentStore paymentStore;

    public PostPayRes execute(Long reservationId) {

        return new PostPayRes();
    }
}
