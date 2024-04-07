package hhplus.serverjava.api.dto.response.reservation;

import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReservationRes {

    private Long reservationId;

    private String concertName;
    private String concertArtist;
    private LocalDateTime reservationDate;

    private int reservationSeat;
    private LocalDateTime expiredAt;
    private int reservedAmount;

    public PostReservationRes(Reservation reservation, Seat seat) {
        this.reservationId = reservation.getId();
        this.concertName = reservation.getConcertName();
        this.concertArtist = reservation.getConcertArtist();
        this.reservationDate = reservation.getConcertAt();
        this.reservationSeat = reservation.getSeatNum();
        this.expiredAt = seat.getExpiredAt();
        this.reservedAmount = reservation.getReservedPrice();
    }
}
