package hhplus.serverjava.domain.reservation.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ReservationTest {
    @DisplayName("setPaid테스트")
    @Test
    void setPaidTest() {
        //given
        LocalDateTime testDateTime = LocalDateTime.now();
        Reservation reservation = Reservation.builder()
                .seatNum(10)
                .reservedPrice(50000)
                .concertAt(testDateTime)
                .concertName("MAKTUB CONCERT")
                .concertArtist("MAKTUB")
                .build();

        //when
        reservation.setPaid();

        //then
        assertEquals(reservation.status, Reservation.State.PAID);
    }

    @DisplayName("setCancelled테스트")
    @Test
    void setCancelledTest() {
        //given
        LocalDateTime testDateTime = LocalDateTime.now();
        Reservation reservation = Reservation.builder()
                .seatNum(10)
                .reservedPrice(50000)
                .concertAt(testDateTime)
                .concertName("MAKTUB CONCERT")
                .concertArtist("MAKTUB")
                .build();

        //when
        reservation.setCancelled();

        //then
        assertEquals(reservation.status, Reservation.State.CANCELLED);
    }


}
