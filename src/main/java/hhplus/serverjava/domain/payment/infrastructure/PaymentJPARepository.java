package hhplus.serverjava.domain.payment.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import hhplus.serverjava.domain.payment.entity.Payment;

public interface PaymentJPARepository extends JpaRepository<Payment, Long> {
}
