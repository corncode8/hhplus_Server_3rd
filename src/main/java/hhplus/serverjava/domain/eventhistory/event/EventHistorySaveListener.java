package hhplus.serverjava.domain.eventhistory.event;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.CREATE_ERROR_EVENT_HISTORY;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import hhplus.serverjava.domain.eventhistory.repository.EventHistoryStoreRepository;
import hhplus.serverjava.domain.payment.PaymentEventPublisher;
import hhplus.serverjava.domain.payment.event.PaymentDataSendEvent;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventHistorySaveListener {

	private final PaymentEventPublisher eventPublisher;
	private final EventHistoryStoreRepository repository;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void eventHistoreySaveHandler(EventHistorySave event) {
		try {
			EventHistory eventHistory = EventHistory.builder()
				.published(false)
				.actor(event.getActor())
				.actorId(event.getPaymentId())
				.eventChannel(event.getEventChannel())
				.build();
			repository.save(eventHistory);

			eventPublisher.paymentDataSendEvent(new PaymentDataSendEvent(eventHistory.getId(), event.getPaymentId()));
		} catch (Exception e) {
			throw new BaseException(CREATE_ERROR_EVENT_HISTORY);
		}
	}
}
