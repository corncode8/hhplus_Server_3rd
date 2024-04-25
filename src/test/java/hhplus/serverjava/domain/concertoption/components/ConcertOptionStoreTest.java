package hhplus.serverjava.domain.concertoption.components;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.repository.ConcertOptionStoreRepository;

@ExtendWith(MockitoExtension.class)
public class ConcertOptionStoreTest {
	@Mock
	ConcertOptionStoreRepository concertOptionStoreRepository;

	@InjectMocks
	ConcertOptionStore concertOptionStore;

	@DisplayName("save테스트")
	@Test
	void saveTest() {
		//given
		Long testId = 1L;
		LocalDateTime testDateTime = LocalDateTime.now();
		ConcertOption concertOption = new ConcertOption(testId, testDateTime);

		when(concertOptionStoreRepository.save(concertOption)).thenReturn(concertOption);

		//when
		ConcertOption result = concertOptionStore.save(concertOption);

		//then
		assertNotNull(result);
		assertEquals(result.getConcertAt(), concertOption.getConcertAt());
	}

}
