package hhplus.serverjava.api.reservation.usecase;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.RESERVED_SEAT;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.reservation.request.PostReservationRequest;
import hhplus.serverjava.api.reservation.response.PostReservationResponse;
import hhplus.serverjava.api.seat.usecase.SeatReservedUseCase;
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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MakeReservationUseCase {

	private final UserReader userReader;
	private final SeatReader seatReader;
	private final SeatReservedUseCase seatReservedUseCase;
	private final ReservationStore reservationStore;
	private final ConcertOptionReader concertOptionReader;

	// 좌석 예약
	public PostReservationResponse makeReservation(Long userId, PostReservationRequest request) {

		LocalDateTime parse = LocalDateTime.parse(request.getTargetDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

		try {
			User user = userReader.findUser(userId);

			// findConcert
			Concert concert = concertOptionReader.findConcert(request.getConcertOptionId());

			// 예약 가능한 좌석 조회
			Seat seat = seatReader.findAvailableSeat(
				request.getConcertOptionId(), parse, Seat.State.AVAILABLE, request.getSeatNum()
			);

			// 예약 생성
			Reservation reservation = ReservationCreator.create(
				concert.getName(), concert.getArtist(), parse, seat.getSeatNum(), seat.getPrice(), user, seat
			);
			reservationStore.save(reservation);

			// 좌석 선점
			seatReservedUseCase.setReserved(seat);
			return new PostReservationResponse(reservation, seat);
		} catch (OptimisticLockingFailureException e) {
			throw new BaseException(RESERVED_SEAT);
		}
	}
}
