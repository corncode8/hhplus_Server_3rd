package hhplus.serverjava.api.usecase.reservation;

import hhplus.serverjava.api.dto.response.reservation.PostReservationRes;
import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionReader;
import hhplus.serverjava.domain.reservation.components.ReservationStore;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.components.SeatReader;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.*;


@Service
@Transactional
@RequiredArgsConstructor
public class MakeReservationUseCase {

    private final SeatReader seatReader;
    private final ReservationStore reservationStore;
    private final ConcertOptionReader concertOptionReader;

    // 좌석 예약
    public PostReservationRes makeReservation(User user, Long concertOptionId, LocalDateTime targetDate, int seatNum) {

        try {
            Seat seat = findAvailableSeat(concertOptionId, targetDate, Seat.State.AVAILABLE, seatNum);

            seat.setReserved();
            seat.setExpiredAt();

            // findConcert
            Concert concert = concertOptionReader.findConcert(concertOptionId);

            // 예약 생성
            Reservation reservation = Reservation.builder()
                    .user(user)
                    .seat(seat)
                    .concertAt(targetDate)
                    .seatNum(seat.getSeatNum())
                    .concertName(concert.getName())
                    .concertArtist(concert.getArtist())
                    .reservedPrice(seat.getPrice())
                    .build();

            reservationStore.save(reservation);

            return new PostReservationRes(reservation, seat);
        } catch (OptimisticLockException e) {
            throw new BaseException(RESERVED_SEAT);
        }
    }

    private Seat findAvailableSeat(Long concertOptionId, LocalDateTime time, Seat.State state, int seatNum) {
        return seatReader.findAvailableSeat(concertOptionId, time, state, seatNum);
    }
}
