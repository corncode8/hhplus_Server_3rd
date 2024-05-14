package hhplus.serverjava.domain.payment.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.payment.reservation.PaymentReaderRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PaymentCoreReaderRepository implements PaymentReaderRepository {

	private final PaymentJpaRepository paymentJpaRepository;

	@Override
	public Optional<Payment> findPayment(Long paymentId) {
		return paymentJpaRepository.findById(paymentId);
	}
}
