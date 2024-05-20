package hhplus.serverjava.domain.payment.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.domain.eventhistory.components.EventHistoryReader;
import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import hhplus.serverjava.domain.eventhistory.repository.EventHistoryStoreRepository;
import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.payment.event.DataSendEvent;
import hhplus.serverjava.domain.payment.event.PaymentSuccessEvent;

@ExtendWith(MockitoExtension.class)
public class PaymentEventServiceTest {

	@Mock
	private EventHistoryStoreRepository historyStoreRepository;

	@Mock
	private EventHistoryReader historyReader;

	@InjectMocks
	private PaymentEventService paymentEventService;

	@DisplayName("findEvent 테스트")
	@Test
	void findEventTest() {
		//given
		Long eventId = 1L;
		EventHistory eventHistory = EventHistory.builder()
			.published(false)
			.build();
		when(historyReader.findEvent(eventId)).thenReturn(eventHistory);

		//when
		EventHistory result = paymentEventService.findEvent(eventId);

		//then
		assertNotNull(result);
		assertEquals(result.getPublished(), eventHistory.getPublished());
	}

	@DisplayName("saveEvent 테스트")
	@Test
	void saveEventTest() {
		//given
		Payment payment = Payment.builder()
			.payAmount(5000L)
			.build();

		PaymentSuccessEvent event = new PaymentSuccessEvent(payment);
		EventHistory eventHistory = EventHistory.builder()
			.published(false)
			.actor(event.getActor())
			.actorId(event.getPayment().getId())
			.build();

		doReturn(eventHistory).when(historyStoreRepository).save(any(EventHistory.class));

		//when
		EventHistory result = paymentEventService.saveEvent(event);

		//then
		assertNotNull(result);
		assertEquals(result.getPublished(), eventHistory.getPublished());
		assertEquals(result.getActor(), eventHistory.getActor());
	}

	@DisplayName("sendData 테스트")
	@Test
	void sendDataTest() {
		//given

		Payment payment = Payment.builder()
			.payAmount(5000L)
			.build();

		EventHistory eventHistory = EventHistory.builder()
			.published(false)
			.actor(EventHistory.Actor.PAY)
			.actorId(payment.getId())
			.build();

		DataSendEvent event = new DataSendEvent(eventHistory.getId(), payment.getId());

		when(historyStoreRepository.findEventHistory(event.getEventId())).thenReturn(Optional.of(eventHistory));
		//when
		paymentEventService.sendData(event);

		//then
		assertEquals(true, eventHistory.getPublished());
	}
}


