package hhplus.serverjava.domain.payment.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import hhplus.serverjava.domain.payment.components.PaymentEventStore;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentSuccessEventListener {

	private final PaymentEventService eventPublisher;
	private final PaymentEventStore paymentEventStore;

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void eventHistoreySaveHandler(PaymentSuccessEvent event) {

		EventHistory eventHistory = paymentEventStore.publishEvent(event);

		eventPublisher.eventPublish(new DataSendEvent(eventHistory.getId(), eventHistory.getActorId()));
	}
}
