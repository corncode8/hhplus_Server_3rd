package hhplus.serverjava.domain.payment;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import hhplus.serverjava.domain.eventhistory.event.EventHistorySave;
import hhplus.serverjava.domain.payment.event.PaymentDataSendEvent;
import hhplus.serverjava.domain.payment.event.PaymentDataSendRetryEvent;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	public void success(EventHistorySave event) {
		applicationEventPublisher.publishEvent(event);
	}

	public void paymentDataSendEvent(PaymentDataSendEvent event) {
		applicationEventPublisher.publishEvent(event);
	}

	public void dataSendRetryEvent(PaymentDataSendRetryEvent event) {
		applicationEventPublisher.publishEvent(event);
	}
}
