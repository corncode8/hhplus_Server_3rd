package hhplus.serverjava.api.dto.response.reservation;

import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReservationRes {

    private Long reservationId;

    private String concertName;
    private String concertArtist;
    private String  reservationDate;

    private int reservationSeat;
    private String  expiredAt;
    private int reservedAmount;

    public PostReservationRes(Reservation reservation, Seat seat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        this.reservationId = reservation.getId();
        this.concertName = reservation.getConcertName();
        this.concertArtist = reservation.getConcertArtist();
        this.reservationDate = reservation.getConcertAt().format(formatter);
        this.reservationSeat = reservation.getSeatNum();
        this.expiredAt = seat.getExpiredAt().format(formatter);
        this.reservedAmount = reservation.getReservedPrice();
    }
}
