package hhplus.serverjava.domain.reservation.components;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationCreator {
	public static Reservation create(
		String concertName,
		String concertArtist,
		LocalDateTime concertAt,
		int seatNum,
		int price,
		User user,
		Seat seat) {
		return Reservation.create(
			concertName, concertArtist, concertAt, seatNum, price, user, seat
		);
	}

}
