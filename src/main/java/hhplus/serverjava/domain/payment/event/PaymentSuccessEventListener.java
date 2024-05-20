package hhplus.serverjava.domain.payment.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import hhplus.serverjava.domain.payment.components.PaymentEventService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentSuccessEventListener {

	private final PaymentEventPublisher eventPublisher;
	private final PaymentEventService paymentEventService;

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void eventHistoreySaveHandler(PaymentSuccessEvent event) {

		EventHistory eventHistory = paymentEventService.saveEvent(event);

		eventPublisher.eventPublish(new DataSendEvent(eventHistory.getId(), eventHistory.getActorId()));
	}
}
