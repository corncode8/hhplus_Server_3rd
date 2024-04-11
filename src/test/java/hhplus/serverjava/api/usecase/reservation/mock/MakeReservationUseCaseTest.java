package hhplus.serverjava.api.usecase.reservation.mock;

import hhplus.serverjava.api.reservation.response.PostReservationRes;
import hhplus.serverjava.api.reservation.usecase.MakeReservationUseCase;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionReader;
import hhplus.serverjava.domain.seat.components.SeatReader;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.jboss.logging.Logger;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MakeReservationUseCaseTest {

    @Mock
    ConcertOptionReader concertOptionReader;
    @Mock
    SeatReader seatReader;
    @InjectMocks
    MakeReservationUseCase makeReservationUseCase;

    private Logger log = Logger.getLogger(MakeReservationUseCaseTest.class);

    /*
    * 예약 생성 테스트
    */
    @DisplayName("예약 생성 테스트")
    @Test
    void test(){
        //given
        Long testId = 1L;
        int testSeatNum = 10;
        LocalDateTime testDateTime = LocalDateTime.now();

        User user = new User(testId, 1000L);

        Seat seat = Seat.builder()
                .id(testId)
                .seatNum(testSeatNum)
                .price(50000)
                .build();
        seat.setReserved();


        Concert concert = new Concert(testId, "IU CONCERT", "IU");


        when(seatReader.findAvailableSeat(testId, testDateTime, Seat.State.AVAILABLE, 10)).thenReturn(seat);
        when(concertOptionReader.findConcert(testId)).thenReturn(concert);

        //when
        PostReservationRes result = makeReservationUseCase.makeReservation(user, testId, testDateTime, testSeatNum);

        //then
        assertNotNull(result);
        assertEquals(seat.getStatus(), Seat.State.RESERVED);
        assertEquals(result.getConcertName(), concert.getName());
        assertEquals(result.getConcertArtist(), concert.getArtist());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        assertEquals(result.getReservationDate(), testDateTime.format(formatter));
        assertEquals(result.getExpiredAt(), seat.getExpiredAt().format(formatter));

        assertEquals(result.getReservationSeat(), testSeatNum);
        assertEquals(result.getReservedAmount(), 50000);

    }

}