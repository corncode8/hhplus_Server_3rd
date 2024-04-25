package hhplus.serverjava.api.usecase.concert;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.api.concert.response.GetConcertResponse;
import hhplus.serverjava.api.concert.usecase.GetConcertListUseCase;
import hhplus.serverjava.domain.concert.components.ConcertReader;
import hhplus.serverjava.domain.concert.entity.Concert;

@ExtendWith(MockitoExtension.class)
public class GetConcertListUseCaseTest {

	@Mock
	ConcertReader concertReader;

	@InjectMocks
	GetConcertListUseCase getConcertListUseCase;

	@DisplayName("콘서트 조회 테스트")
	@Test
	void test() {
		//given
		List<Concert> concertList = Arrays.asList(
			new Concert(1L, "MAKTUB CONCERT", "MAKTUB"),
			new Concert(2L, "IU CONCERT", "IU"),
			new Concert(3L, "MAKTUB CONCERT", "MAKTUB"),
			new Concert(4L, "IU CONCERT", "IU"),
			new Concert(5L, "MAKTUB CONCERT", "MAKTUB")
		);

		when(concertReader.findConcertList(Concert.State.SHOWING)).thenReturn(concertList);

		//when
		GetConcertResponse result = getConcertListUseCase.execute();

		//then
		assertNotNull(result);
		assertEquals(result.getConcertInfoList().size(), concertList.size());
		assertEquals(result.getConcertInfoList().get(1).getConcertName(), concertList.get(1).getName());
	}
}
