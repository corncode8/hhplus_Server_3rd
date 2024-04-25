package hhplus.serverjava.domain.reservation.components;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.reservation.repository.ReservationReaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationReader {
	private final ReservationReaderRepository reservationReaderRepository;

	public Reservation findReservation(Long reservationId) {
		return reservationReaderRepository.findReservation(reservationId)
			.orElseThrow(() -> new BaseException(INVALID_RESERVATION));
	}

	// 스케줄러 로직
	public List<Reservation> findExpiredReservaions(LocalDateTime now) {
		return reservationReaderRepository.findExpiredReservaions(now);
	}
}
