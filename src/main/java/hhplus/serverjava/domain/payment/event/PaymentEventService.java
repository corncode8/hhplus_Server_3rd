package hhplus.serverjava.domain.payment.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventService {

	private final ApplicationEventPublisher applicationEventPublisher;

	public void eventPublish(PaymentSuccessEvent event) {
		applicationEventPublisher.publishEvent(event);
	}

	public void eventPublish(DataSendEvent event) {
		applicationEventPublisher.publishEvent(event);
	}
}
