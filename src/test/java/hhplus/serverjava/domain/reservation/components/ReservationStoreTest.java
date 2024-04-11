package hhplus.serverjava.domain.reservation.components;

import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.reservation.repository.ReservationStoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        Long testId = 1L;
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

}
