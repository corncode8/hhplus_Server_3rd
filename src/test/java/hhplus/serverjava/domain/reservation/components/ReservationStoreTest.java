package hhplus.serverjava.domain.reservation.components;

import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.reservation.repository.ReservationStoreRepository;
import hhplus.serverjava.domain.seat.entity.Seat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationStoreTest {

    @Mock
    ReservationStoreRepository repository;

    @InjectMocks
    ReservationStore reservationStore;

    @DisplayName("테스트")
    @Test
    void test() {
        //given
        LocalDateTime testDateTime = LocalDateTime.now();
        Reservation reservation = Reservation.builder()
                .seatNum(10)
                .reservedPrice(50000)
                .concertAt(testDateTime)
                .concertName("MAKTUB CONCERT")
                .concertArtist("MAKTUB")
                .build();

        when(repository.save(reservation)).thenReturn(reservation);

        //when
        Reservation result = reservationStore.save(reservation);

        //then
        assertNotNull(result);
        assertEquals(result.getReservedPrice(), reservation.getReservedPrice());
        assertEquals(result.getConcertArtist(), reservation.getConcertArtist());
        assertEquals(result.getConcertAt(), reservation.getConcertAt());
    }

    @DisplayName("ExpireReservation테스트")
    @Test
    void ExpireReservationTest() {
        //given

        Long testId = 1L;
        Seat seat = new Seat(testId, 50, 50000);

        LocalDateTime testDateTime = LocalDateTime.now().plusDays(1);
        List<Reservation> reservationList = Arrays.asList(
                new Reservation("MAKTUB", "MAKTUB CONCERT", testDateTime, seat),
                new Reservation("MAKTUB", "MAKTUB CONCERT", testDateTime, seat),
                new Reservation("MAKTUB", "MAKTUB CONCERT", testDateTime, seat),
                new Reservation("MAKTUB", "MAKTUB CONCERT", testDateTime, seat),
                new Reservation("MAKTUB", "MAKTUB CONCERT", testDateTime, seat)
        );
        //when
        reservationStore.ExpireReservation(reservationList);

        //then

        // reservation Status 검증
        assertTrue(reservationList.stream().allMatch(r -> r.getStatus() == Reservation.State.CANCELLED));

        // seat Status 검증
        assertTrue(reservationList.stream().allMatch(r -> r.getSeat().getStatus() == Seat.State.AVAILABLE));
    }

}
