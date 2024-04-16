package hhplus.serverjava.domain.seat.components;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.seat.repository.SeatReaderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SeatReaderTest {

    @Mock
    SeatReaderRepository seatReaderRepository;

    @InjectMocks
    SeatReader seatReader;

    @DisplayName("findAvailableSeats테스트")
    @Test
    void findAvailableSeatsTest() {
        //given
        Long testId = 1L;
        LocalDateTime testDateTime = LocalDateTime.now();

        List<Seat> seatList = Arrays.asList(
                new Seat(1L, 10, 50000),
                new Seat(2L, 10, 50000),
                new Seat(3L, 10, 50000),
                new Seat(4L, 10, 50000),
                new Seat(5L, 10, 50000)
        );

        when(seatReaderRepository.findAvailableSeats(testId, testDateTime, Seat.State.AVAILABLE)).thenReturn(seatList);

        //when
        List<Seat> result = seatReader.findAvailableSeats(testId, testDateTime, Seat.State.AVAILABLE);

        //then
        assertNotNull(result);
        assertEquals(result.size(), seatList.size());
    }

    @DisplayName("findAvailableSeatsEmtpy테스트")
    @Test
    void findAvailableSeatsEmptyTest() {
        //given
        Long testId = 1L;
        LocalDateTime testDateTime = LocalDateTime.now();

        List<Seat> seatList = new ArrayList<>();

        when(seatReaderRepository.findAvailableSeats(testId, testDateTime, Seat.State.AVAILABLE)).thenReturn(seatList);

        //when & then
        BaseException exception = assertThrows(BaseException.class, () -> seatReader.findAvailableSeats(testId, testDateTime, Seat.State.AVAILABLE));
        assertEquals(EMPTY_SEAT_RESERVATION.getMessage(), exception.getMessage());

    }

    @DisplayName("findAvailableSeat테스트")
    @Test
    void findAvailableSeatTest() {
        //given
        Long testId = 1L;
        LocalDateTime testDateTime = LocalDateTime.now();
        Seat seat = new Seat(testId, 50, 50000);

        when(seatReaderRepository.findAvailableSeat(testId, testDateTime, Seat.State.AVAILABLE, 50)).thenReturn(Optional.of(seat));

        //when
        Seat result = seatReader.findAvailableSeat(testId, testDateTime, Seat.State.AVAILABLE, 50);

        //then
        assertNotNull(result);
        assertEquals(result.getSeatNum(), seat.getSeatNum());
        assertEquals(result.getPrice(), seat.getPrice());
    }

    @DisplayName("findAvailableSeat_NotFound_테스트")
    @Test
    void findAvailableSeatNotFoundTest() {
        //given
        Long testId = 1L;
        LocalDateTime testDateTime = LocalDateTime.now();


        //when & then
        BaseException exception = assertThrows(BaseException.class, () -> seatReader.findAvailableSeat(testId, testDateTime, Seat.State.AVAILABLE, 50));
        assertEquals(INVALID_SEAT_RESERVATION.getMessage(), exception.getMessage());
    }

    @DisplayName("findSeatById테스트")
    @Test
    void findSeatByIdTest() {
        //given
        Long testId = 1L;

        Seat seat = new Seat(testId, 50, 50000);

        when(seatReaderRepository.findSeatById(testId)).thenReturn(Optional.of(seat));

        //when
        Seat result = seatReader.findSeatById(testId);

        //then
        assertNotNull(result);
        assertEquals(result.getSeatNum(), seat.getSeatNum());
        assertEquals(result.getPrice(), seat.getPrice());
    }

    @DisplayName("findSeatById_NotFound_테스트")
    @Test
    void findSeatById_NotFound_Test() {
        //given
        Long testId = 1L;

        //when & then
        BaseException exception = assertThrows(BaseException.class, () -> seatReader.findSeatById(testId));
        assertEquals(INVALID_SEAT.getMessage(), exception.getMessage());
    }

}
