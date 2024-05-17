package hhplus.serverjava.domain.payment.infrastructure;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.payment.repository.PaymentStoreRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PaymentCoreStoreRepository implements PaymentStoreRepository {
	private final PaymentJpaRepository paymentJpaRepository;

	public Payment save(Payment payment) {
		return paymentJpaRepository.save(payment);
	}
}
