package hhplus.serverjava.api.usecase.reservation;

import hhplus.serverjava.api.dto.response.reservation.GetDateRes;
import hhplus.serverjava.api.usecase.concert.FindConcertOptionUseCase;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionReader;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

        Long optionId = 1L;
        LocalDateTime concertAt = LocalDateTime.now();

        Long plusDays = 5L;

        ConcertOption concertOption = ConcertOption.builder()
                .id(optionId)
                .concertAt(concertAt)
                .build();

        List<ConcertOption> concertOptionList = new ArrayList<>();
        concertOptionList.add(concertOption);

        when(concertOptionReader.findConcertOptionList(testConcertId)).thenReturn(concertOptionList);

        List<LocalDate> list = new ArrayList<>();
        for (int i = 0; i < plusDays; i++) {
            list.add(LocalDate.now().plusDays(i));
        }

        //when
        GetDateRes result = findConcertOptionUseCase.execute(testConcertId);


        //then
        assertNotNull(result);
        assertEquals(result.getAvailableDates(), list);
    }
}
