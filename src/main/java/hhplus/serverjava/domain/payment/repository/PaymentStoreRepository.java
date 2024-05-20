package hhplus.serverjava.domain.payment.repository;

import hhplus.serverjava.domain.payment.entity.Payment;

public interface PaymentStoreRepository {

	Payment save(Payment payment);
}
