package hhplus.serverjava.domain.payment.event;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FOUND_EVENT_HISTORY;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import hhplus.serverjava.domain.eventhistory.repository.EventHistoryReaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentDataSendRetryListener {

	private final EventHistoryReaderRepository eventHistoryReaderRepository;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void PaymentDataSendhandler(PaymentDataSendEvent event) {
		EventHistory eventHistory = eventHistoryReaderRepository.findEventHistory(event.getEventId())
			.orElseThrow(() -> new BaseException(NOT_FOUND_EVENT_HISTORY));
		try {
			// 데이터 플랫폼에 정보 전송

			// 이벤트 발행 성공
			eventHistory.setTruePublished();
		} catch (Exception e) {
			// retry 이벤트 오류
			log.error("데이터 플랫폼 전송 오류");
			eventHistory.setReason(e.getMessage());
		}
	}
}
