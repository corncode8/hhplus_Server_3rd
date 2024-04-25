package hhplus.serverjava.domain.payment.reservation;

import hhplus.serverjava.domain.payment.entity.Payment;

public interface PaymentStoreRepository {

	Payment save(Payment payment);
}
