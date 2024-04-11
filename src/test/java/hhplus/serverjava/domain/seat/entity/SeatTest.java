package hhplus.serverjava.domain.seat.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SeatTest {

    @DisplayName("setReserved테스트")
    @Test
    void setReservedTest() {
        //given
        Long testId = 1L;

        Seat seat = new Seat(testId, 50, 50000);

        //when
        LocalDateTime testDateTime = LocalDateTime.now().plusMinutes(5);
        seat.setReserved();

        //then
        assertEquals(Seat.State.RESERVED, seat.getStatus());
        assertEquals(testDateTime, seat.getExpiredAt());
    }

    @DisplayName("setAvailable테스트")
    @Test
    void setAvailableTest() {
        //given
        Long testId = 1L;

        Seat seat = new Seat(testId, 50, 50000);

        //when
        seat.setAvailable();

        //then
        assertEquals(Seat.State.AVAILABLE, seat.getStatus());
    }

}
