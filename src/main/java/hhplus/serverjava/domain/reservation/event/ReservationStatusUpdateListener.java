package hhplus.serverjava.domain.reservation.event;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FOUND_PAYMENT;

import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.payment.event.PaymentSuccessEvent;
import hhplus.serverjava.domain.payment.reservation.PaymentReaderRepository;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationStatusUpdateListener {
	private final PaymentReaderRepository paymentReaderRepository;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Order(1)
	public void paymentSuccessHandler(PaymentSuccessEvent event) {
		// 주문 상태 변경
		Payment payment = paymentReaderRepository.findPayment(event.getPaymentId())
			.orElseThrow(() -> new BaseException(NOT_FOUND_PAYMENT));
		Reservation reservation = payment.getReservation();
		reservation.setPaid();
	}
}
