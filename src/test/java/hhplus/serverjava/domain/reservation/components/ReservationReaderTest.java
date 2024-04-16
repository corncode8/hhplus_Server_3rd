package hhplus.serverjava.domain.reservation.components;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.reservation.repository.ReservationReaderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.INVALID_RESERVATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationReaderTest {

    @Mock
    ReservationReaderRepository reservationReaderRepository;

    @InjectMocks
    ReservationReader reservationReader;

    @DisplayName("findReservation테스트")
    @Test
    void findReservationTest() {
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

        when(reservationReaderRepository.findReservation(testId)).thenReturn(Optional.of(reservation));

        //when
        Reservation result = reservationReader.findReservation(testId);

        //then
        assertNotNull(result);
        assertEquals(result.getConcertAt(), reservation.getConcertAt());
        assertEquals(result.getReservedPrice(), reservation.getReservedPrice());
    }

    @DisplayName("findReservation_NotFound_테스트")
    @Test
    void findReservationNotFoundTest() {
        // given
        Long testId = 1L;
        when(reservationReaderRepository.findReservation(testId)).thenReturn(Optional.empty());

        // when & then
        BaseException exception = assertThrows(BaseException.class, () -> reservationReader.findReservation(testId));
        assertEquals(INVALID_RESERVATION.getMessage(), exception.getMessage());
    }

}
