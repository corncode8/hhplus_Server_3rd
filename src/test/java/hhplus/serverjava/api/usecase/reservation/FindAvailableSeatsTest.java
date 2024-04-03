package hhplus.serverjava.api.usecase.reservation;

import hhplus.serverjava.api.domain.dto.response.reservation.GetSeatsRes;
import hhplus.serverjava.api.domain.usecase.concert.FindAvailableSeats;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindAvailableSeatsTest {

    @Mock
    SeatReader seatReader;
    @Mock
    ConcertOptionReader concertOptionReader;

    @InjectMocks
    FindAvailableSeats findAvailableSeats;

    @DisplayName("예약 가능한 좌석 API 테스트")
    @Test
    void test() {
        //given
        Long concertId = 1L;
        LocalDate targetDate = LocalDate.now().plusDays(1);

        Long optionId = 1L;
        LocalDate startDate = LocalDate.now();

        Long plusDays = 5L;
        LocalDate endDate = LocalDate.now().plusDays(plusDays);
        int seatNum = 50;

        ConcertOption concertOption = ConcertOption.builder()
                .id(optionId)
                .startedAt(startDate)
                .endedAt(endDate)
                .seatsNum(seatNum)
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

        when(concertOptionReader.findConcertOption(optionId)).thenReturn(concertOption);
        when(seatReader.findAvailableSeats(concertId, targetDate, Seat.State.AVAILABLE)).thenReturn(seatList);

        //when
        GetSeatsRes result = findAvailableSeats.execute(concertId, targetDate.toString());

        //then
        assertNotNull(result);
        assertEquals(result.getAvailableSeatsList(), seatList);
    }
}
