package hhplus.serverjava.domain.payment.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	public void success(PaymentSuccessEvent event) {
		applicationEventPublisher.publishEvent(event);
	}
}
