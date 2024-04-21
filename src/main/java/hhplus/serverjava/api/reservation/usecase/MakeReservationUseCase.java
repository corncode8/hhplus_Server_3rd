package hhplus.serverjava.api.reservation.usecase;

import hhplus.serverjava.api.reservation.request.PostReservationRequest;
import hhplus.serverjava.api.reservation.response.PostReservationResponse;
import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionReader;
import hhplus.serverjava.domain.reservation.components.ReservationCreator;
import hhplus.serverjava.domain.reservation.components.ReservationStore;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.components.SeatReader;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.*;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MakeReservationUseCase {

    private final UserReader userReader;
    private final SeatReader seatReader;
    private final ReservationStore reservationStore;
    private final ConcertOptionReader concertOptionReader;

    // 좌석 예약
    public PostReservationResponse makeReservation(Long userId, PostReservationRequest request){

        LocalDateTime parse = LocalDateTime.parse(request.getTargetDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        try {
            User user = userReader.findUser(userId);

            Seat seat = seatReader.findAvailableSeat(request.getConcertOptionId(), parse, Seat.State.AVAILABLE, request.getSeatNum());
            log.info("Seat 상태 변경 전 : {}", seat.getStatus());

            // 좌석 예약상태로 변경, 임시 배정시간 5분 Set
            seat.setReserved();

            log.info("Seat 상태 변경 후 : {}", seat.getStatus());

            // findConcert
            Concert concert = concertOptionReader.findConcert(request.getConcertOptionId());

            // 예약 생성
            Reservation reservation = ReservationCreator.create(
                    concert.getName(), concert.getArtist(), parse, seat.getSeatNum(), seat.getPrice(), user, seat
            );

            reservationStore.save(reservation);

            return new PostReservationResponse(reservation, seat);
        } catch (OptimisticLockException e) {
            log.info("OptimisticLockException : {}", e.getMessage());
            throw new BaseException(RESERVED_SEAT);
        }
    }
}
