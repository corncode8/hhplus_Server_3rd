package hhplus.serverjava.api.usecase.concert;

import hhplus.serverjava.api.concert.usecase.FindAvailableSeatsUseCase;
import hhplus.serverjava.api.seat.response.GetSeatsRes;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionReader;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.seat.components.SeatReader;
import hhplus.serverjava.domain.seat.entity.Seat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindAvailableSeatsUseCaseTest {

    @Mock
    SeatReader seatReader;
    @Mock
    ConcertOptionReader concertOptionReader;

    @InjectMocks
    FindAvailableSeatsUseCase findAvailableSeatsUseCase;

    @DisplayName("예약 가능한 좌석 API 테스트")
    @Test
    void test() {
        //given
        Long concertId = 1L;
        LocalDateTime targetDate = LocalDateTime.now().plusDays(1);

        Long optionId = 1L;
        LocalDateTime concertAt = LocalDateTime.now().plusDays(1);

        ConcertOption concertOption = ConcertOption.builder()
                .id(optionId)
                .concertAt(concertAt)
                .build();

        List<Seat> seatList = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            Long id = i + 1L;
            Seat seat = Seat.builder()
                    .id(id)
                    .price(5000)
                    .seatNum(i + 1)
                    .build();
            seatList.add(seat);
        }

        when(concertOptionReader.findConcertOption(optionId, targetDate)).thenReturn(concertOption);
        when(seatReader.findAvailableSeats(concertId, targetDate, Seat.State.AVAILABLE)).thenReturn(seatList);

        //when
        GetSeatsRes result = findAvailableSeatsUseCase.execute(concertId, targetDate);

        //then
        assertNotNull(result);

        // 예약 가능한 좌석 equals 검증
        assertEquals(result.getSeatList(), seatList.stream().map(seat -> seat.getSeatNum()).collect(Collectors.toList()));
    }
}
