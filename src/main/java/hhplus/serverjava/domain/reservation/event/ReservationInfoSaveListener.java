package hhplus.serverjava.domain.reservation.event;

import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import hhplus.serverjava.domain.payment.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationInfoSaveListener {

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Order(2)
	public void paymentSuccessHandler(PaymentSuccessEvent event) {
		// 데이터 플랫폼에 정보 전송

	}
}
