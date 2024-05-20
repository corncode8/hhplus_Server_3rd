package hhplus.serverjava.domain.eventhistory.components;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.support.response.BaseResponseStatus;
import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import hhplus.serverjava.domain.eventhistory.repository.EventHistoryReaderRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventHistoryReader {

	private final EventHistoryReaderRepository repository;

	public List<EventHistory> failEventList(LocalDateTime now) {
		return repository.failEventList(now);
	}

	public EventHistory findEvent(Long eventId) {
		return repository.findEventHistory(eventId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_EVENT_HISTORY));
	}
}
