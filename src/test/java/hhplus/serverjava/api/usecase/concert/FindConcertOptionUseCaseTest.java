package hhplus.serverjava.api.usecase.concert;

import hhplus.serverjava.api.concert.usecase.FindConcertOptionUseCase;
import hhplus.serverjava.api.reservation.response.GetDateResponse;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionReader;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindConcertOptionUseCaseTest {

    @Mock
    ConcertOptionReader concertOptionReader;

    @InjectMocks
    FindConcertOptionUseCase findConcertOptionUseCase;

    @DisplayName("예약 가능 날짜 조회 API 테스트")
    @Test
    void test() {
        //given
        Long testConcertId = 1L;
        LocalDateTime testDateTime = LocalDateTime.now();

        ConcertOption concertOption1 = new ConcertOption(1L, testDateTime.plusDays(1));
        ConcertOption concertOption2 = new ConcertOption(2L, testDateTime.plusDays(2));
        ConcertOption concertOption3 = new ConcertOption(3L, testDateTime.plusDays(3));

        List<ConcertOption> concertOptionList = new ArrayList<>();
        concertOptionList.add(concertOption1);
        concertOptionList.add(concertOption2);
        concertOptionList.add(concertOption3);

        when(concertOptionReader.findConcertOptionList(testConcertId)).thenReturn(concertOptionList);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<String> list = Arrays.asList(
                testDateTime.plusDays(1).format(formatter),
                testDateTime.plusDays(2).format(formatter),
                testDateTime.plusDays(3).format(formatter)
        );

        //when
        GetDateResponse result = findConcertOptionUseCase.execute(testConcertId);


        //then
        assertNotNull(result);
        assertEquals(result.getAvailableDates(), list);
    }
}
