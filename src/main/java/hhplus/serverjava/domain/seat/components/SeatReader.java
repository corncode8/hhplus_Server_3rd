package hhplus.serverjava.domain.seat.components;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.seat.repository.SeatReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.EMPTY_SEAT_RESERVATION;
import static hhplus.serverjava.api.support.response.BaseResponseStatus.INVALID_SEAT_RESERVATION;
import static hhplus.serverjava.api.support.response.BaseResponseStatus.INVALID_SEAT;


@Component
@RequiredArgsConstructor
public class SeatReader {

    private final SeatReaderRepository seatReaderRepository;

    public List<Seat> findAvailableSeats(Long concertId, LocalDateTime targetDate, Seat.State state) {
        List<Seat> availableSeats = seatReaderRepository.findAvailableSeats(concertId, targetDate, state);

        if (availableSeats.isEmpty()) {
            throw new BaseException(EMPTY_SEAT_RESERVATION);
        }

        return seatReaderRepository.findAvailableSeats(concertId, targetDate, state);
    }

    public Seat findAvailableSeat(Long concertOptionId, LocalDateTime targetDate, Seat.State state, int seatNum) {
        return seatReaderRepository.findAvailableSeat(concertOptionId, targetDate, state, seatNum)
                .orElseThrow(() -> new BaseException(INVALID_SEAT_RESERVATION));
    }

    public Seat findSeatById(Long seatId) {
        return seatReaderRepository.findSeatById(seatId)
                .orElseThrow(() -> new BaseException(INVALID_SEAT));
    }
}
