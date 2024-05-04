package hhplus.serverjava.domain.payment.components;

import org.springframework.stereotype.Component;

import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentCreator {
	public static Payment create(
		Long payAmount,
		Reservation reservation
	) {
		return Payment.create(
			payAmount, reservation
		);
	}
}
