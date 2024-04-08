package hhplus.serverjava.domain.payment.infrastructure;

import hhplus.serverjava.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJPARepository extends JpaRepository<Payment, Long> {
}
