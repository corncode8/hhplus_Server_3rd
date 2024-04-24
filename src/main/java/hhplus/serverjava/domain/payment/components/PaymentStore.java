package hhplus.serverjava.domain.payment.components;

import org.springframework.stereotype.Component;

import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.payment.reservation.PaymentStoreRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentStore {

	private final PaymentStoreRepository paymentStoreRepository;

	public Payment save(Payment payment) {
		return paymentStoreRepository.save(payment);
	}
}
