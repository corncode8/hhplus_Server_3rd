package hhplus.serverjava.domain.payment.event;

import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import hhplus.serverjava.domain.payment.components.PaymentEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSendListener {

	private final PaymentEventService paymentEventService;

	@Async
	@Retryable(maxAttempts = 2)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void PaymentDataSendhandler(DataSendEvent event) {
		EventHistory eventHistory = paymentEventService.findEvent(event.getEventId());

		try {
			// 데이터 플랫폼에 정보 전송 + 이벤트 성공
			paymentEventService.sendData(event);

		} catch (Exception e) {
			eventHistory.setReason(e.getMessage());
			log.error("DataSendListener 실패 : {}", e.getMessage());
		}

	}
}
