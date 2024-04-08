package hhplus.serverjava.domain.payment.infrastructure;

import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.payment.reservation.PaymentStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class PaymentCoreStoreRepository implements PaymentStoreRepository {

    private final PaymentJPARepository paymentJPARepository;

    public Payment save(Payment payment) {
        return paymentJPARepository.save(payment);
    }
}
