package hhplus.serverjava.domain.eventhistory.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.domain.eventhistory.entity.EventHistory;
import hhplus.serverjava.domain.eventhistory.repository.EventHistoryReaderRepository;

@ExtendWith(MockitoExtension.class)
public class EventHistoryReaderTest {

	@Mock
	private EventHistoryReaderRepository repository;

	@InjectMocks
	private EventHistoryReader eventHistoryReader;

	@DisplayName("failEventList 테스트")
	@Test
	void failEventListTest() {
		//given
		LocalDateTime now = LocalDateTime.now();
		List<EventHistory> eventHistoryList = Arrays.asList(
			EventHistory.builder()
				.published(false)
				.build(),
			EventHistory.builder()
				.published(false)
				.build(),
			EventHistory.builder()
				.published(false)
				.build()
		);
		when(repository.failEventList(now)).thenReturn(eventHistoryList);

		//when
		List<EventHistory> result = eventHistoryReader.failEventList(now);

		//then
		assertNotNull(result);
		assertEquals(result.size(), eventHistoryList.size());
	}

	@DisplayName("findEvent 테스트")
	@Test
	void findEventTest() {
		//given
		Long eventId = 1L;
		EventHistory eventHistory = EventHistory.builder()
			.published(false)
			.build();
		when(repository.findEventHistory(eventId)).thenReturn(Optional.ofNullable(eventHistory));

		//when
		EventHistory result = eventHistoryReader.findEvent(eventId);

		//then
		assertNotNull(result);
		assertEquals(result.getPublished(), result.getPublished());
	}

}
