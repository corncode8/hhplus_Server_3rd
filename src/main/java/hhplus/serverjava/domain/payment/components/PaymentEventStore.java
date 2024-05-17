package hhplus.serverjava.domain.payment.components;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FOUND_EVENT_HISTORY;

import org.springframework.stereotype.Component;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import hhplus.serverjava.domain.eventhistory.repository.EventHistoryStoreRepository;
import hhplus.serverjava.domain.payment.event.DataSendEvent;
import hhplus.serverjava.domain.payment.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventStore {

	private final EventHistoryStoreRepository repository;

	public EventHistory publishEvent(PaymentSuccessEvent event) {
		EventHistory eventHistory = EventHistory.builder()
			.published(false)
			.actor(event.getActor())
			.actorId(event.getPaymentId())
			.eventChannel(event.getEventChannel())
			.build();
		return repository.save(eventHistory);
	}

	public void sendData(DataSendEvent event) {
		EventHistory eventHistory = repository.findEventHistory(event.getEventId())
			.orElseThrow(() -> new BaseException(NOT_FOUND_EVENT_HISTORY));

		// 데이터 플랫폼에 결제 데이터 전송

		// 이벤트 성공
		eventHistory.setTruePublished();
	}
}
