package hhplus.serverjava.api.usecase.concert;

import hhplus.serverjava.api.dto.response.concert.GetConcertRes;
import hhplus.serverjava.domain.concert.components.ConcertReader;
import hhplus.serverjava.domain.concert.entity.Concert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetConcertListUseCaseTest {

    @Mock
    ConcertReader concertReader;

    @InjectMocks
    GetConcertListUseCase getConcertListUseCase;

    @DisplayName("테스트")
    @Test
    void test() {
        //given
        List<Concert> concertList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Concert concert = new Concert(i + 1L, "MAKTUB CONCERT", "MAKTUB");
            concertList.add(concert);
        }

        when( concertReader.findConcertList(Concert.State.SHOWING)).thenReturn(concertList);

        //when
        GetConcertRes result = getConcertListUseCase.execute(Concert.State.SHOWING);

        //then
        assertNotNull(result);
        assertEquals(result.getConcertInfoList().size(), concertList.size());
    }
}
