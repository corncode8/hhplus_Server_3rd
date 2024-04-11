package hhplus.serverjava.domain.concertoption.entity;

import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.seat.entity.Seat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ConcertOptionTest {
    
    @DisplayName("addConcert테스트")
    @Test
    void addConcertTest() {
        //given
        Long concertId = 1L;
        String concertName = "MAKTUB CONCERT";
        String artist = "MAKTUB";
        Concert concert = new Concert(concertId, concertName, artist);

        Long concertOptionId = 1L;
        LocalDateTime testDateTime = LocalDateTime.now();
        ConcertOption concertOption =  new ConcertOption(concertOptionId, testDateTime);

        //when
        concertOption.addConcert(concert);
        
        //then
        assertEquals(concertOption.getConcert(), concert);
    }

    @DisplayName("addSeatList테스트")
    @Test
    void addSeatListTest() {
        //given
        Long testId = 1L;
        Seat seat = Seat.builder()
                .id(testId)
                .price(5000)
                .seatNum(5)
                .build();

        Long concertOptionId = 1L;
        LocalDateTime testDateTime = LocalDateTime.now();
        ConcertOption concertOption =  new ConcertOption(concertOptionId, testDateTime);

        //when
        concertOption.addSeatList(seat);

        //then
        assertEquals(concertOption.getSeatList().get(0), seat);
    }

}
