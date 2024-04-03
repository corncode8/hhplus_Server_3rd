package hhplus.serverjava.api.domain.usecase.concert;

import hhplus.serverjava.api.domain.dto.response.reservation.GetSeatsRes;
import hhplus.serverjava.common.exceptions.BaseException;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionReader;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.seat.components.SeatReader;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static hhplus.serverjava.common.response.BaseResponseStatus.*;

@Transactional
@RequiredArgsConstructor
public class FindAvailableSeats {

    private SeatReader seatReader;
    private ConcertOptionReader concertOptionReader;

    public GetSeatsRes execute(Long concertId, String targetDate) {
        LocalDate target = dateParser(targetDate);

        ConcertOption concertOption = concertOptionReader.findConcertOption(concertId);
        isVaild(concertOption.getSeatsNum());

        List<Seat> availableSeats = seatReader.findAvailableSeats(concertId, target, Seat.State.AVAILABLE);

        return new GetSeatsRes(availableSeats);
    }

    private LocalDate dateParser(String StrDate) {
        return LocalDate.parse(StrDate, DateTimeFormatter.ISO_DATE);
    }

    private Boolean isVaild(int seatNum) {

        // 예약시 seatNum 을 줄인다는 가정
        if (seatNum <= 0) {
            throw new BaseException(EMPTY_SEAT_RESERVATION);
        }

        return true;
    }
}
