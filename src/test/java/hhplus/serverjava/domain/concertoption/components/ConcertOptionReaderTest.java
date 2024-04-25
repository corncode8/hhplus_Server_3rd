package hhplus.serverjava.domain.concertoption.components;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.repository.ConcertOptionReaderRepository;

@ExtendWith(MockitoExtension.class)
public class ConcertOptionReaderTest {
	@Mock
	ConcertOptionReaderRepository concertOptionReaderRepository;

	@InjectMocks
	ConcertOptionReader concertOptionReader;

	@DisplayName("findConcertOptionList_테스트")
	@Test
	void findConcertOptionListTest() {
		//given
		Long concertOptionId = 1L;
		List<ConcertOption> concertOptionList = Arrays.asList(
			new ConcertOption(1L, LocalDateTime.now()),
			new ConcertOption(2L, LocalDateTime.now()),
			new ConcertOption(3L, LocalDateTime.now()),
			new ConcertOption(4L, LocalDateTime.now()),
			new ConcertOption(5L, LocalDateTime.now())
		);

		when(concertOptionReaderRepository.findConcertOptionList(concertOptionId)).thenReturn(concertOptionList);

		//when
		List<ConcertOption> result = concertOptionReader.findConcertOptionList(concertOptionId);

		//then
		assertNotNull(result);
		assertEquals(result.size(), concertOptionList.size());
	}

	@DisplayName("findConcertOptionList_Empty_테스트")
	@Test
	void findConcertOptionListEmptyTest() {
		//given
		Long concertOptionId = 1L;
		List<ConcertOption> concertOptionList = new ArrayList<>();

		when(concertOptionReaderRepository.findConcertOptionList(concertOptionId)).thenReturn(concertOptionList);

		//when & then
		BaseException exception = assertThrows(BaseException.class,
			() -> concertOptionReader.findConcertOptionList(concertOptionId));
		assertEquals(NOT_FOUND_CONCERT_OPTION.getMessage(), exception.getMessage());
	}

	@DisplayName("findConcertOption 테스트")
	@Test
	void findConcertOptionTest() {
		//given
		Long concertOptionId = 1L;
		LocalDateTime testDateTime = LocalDateTime.now();
		ConcertOption concertOption = new ConcertOption(concertOptionId, testDateTime);

		when(concertOptionReaderRepository.findConcertOption(concertOptionId, testDateTime)).thenReturn(
			Optional.of(concertOption));

		//when
		ConcertOption result = concertOptionReader.findConcertOption(concertOptionId, testDateTime);

		//then
		assertNotNull(result);
		assertEquals(result.getConcertAt(), concertOption.getConcertAt());
	}

	@DisplayName("findConcertOption_NotFound_테스트")
	@Test
	void findConcertOptionNotFoundTest() {
		// given
		Long concertOptionId = 1L;
		LocalDateTime testDateTime = LocalDateTime.now();

		// when & then
		BaseException exception = assertThrows(BaseException.class,
			() -> concertOptionReader.findConcertOption(concertOptionId, testDateTime));
		assertEquals(NOT_FOUND_CONCERT_OPTION.getMessage(), exception.getMessage());
	}

	@DisplayName("findConcert테스트")
	@Test
	void findConcertTest() {
		//given
		Long concertOptionId = 1L;
		Long concertId = 1L;
		String concertName = "MAKTUB CONCERT";
		String artist = "MAKTUB";

		Concert concert = new Concert(concertId, concertName, artist);

		when(concertOptionReaderRepository.findConcert(concertOptionId)).thenReturn(Optional.of(concert));

		//when
		Concert result = concertOptionReader.findConcert(concertOptionId);

		//then
		assertNotNull(result);
		assertEquals(result.getArtist(), concert.getArtist());
		assertEquals(result.getName(), concert.getName());
	}

	@DisplayName("findConcert_NotFound_테스트")
	@Test
	void findConcertNotFoundTest() {
		// given
		Long concertId = 1L;

		// when & then
		BaseException exception = assertThrows(BaseException.class, () -> concertOptionReader.findConcert(concertId));
		assertEquals(NOT_FOUND_CONCERT_OPTION.getMessage(), exception.getMessage());
	}

}
