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
            Seat seat = seatReader.findAvailableSeat(concertOptionId, targetDate, Seat.State.AVAILABLE, seatNum);

            seat.setReserved();
            seat.setExpiredAt();

            // findConcert
            Concert concert = concertOptionReader.findConcert(concertOptionId);

            Reservation reservation = Reservation.builder()
                    .user(user)
                    .seat(seat)
                    .concertAt(targetDate)
                    .concertName(concert.getName())
                    .concertArtist(concert.getArtist())
                    .reservedPrice(seat.getPrice())
                    .build();

            Reservation store = reservationStore.makeReservation(reservation);

            return new PostReservationRes(store, seat);
        } catch (OptimisticLockException e) {
            throw new BaseException(RESERVED_SEAT);
        }
    }

}
