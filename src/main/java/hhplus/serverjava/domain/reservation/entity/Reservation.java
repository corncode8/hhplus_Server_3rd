package hhplus.serverjava.domain.reservation.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import hhplus.serverjava.domain.common.BaseEntity;
import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reservation_id", nullable = false, updatable = false)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	protected State status = State.RESERVED;

	@Column(nullable = false)
	private String concertName;

	@Column(nullable = false, length = 50)
	private String concertArtist;

	@Column(nullable = false)
	private LocalDateTime concertAt;

	@Column(nullable = false)
	private int seatNum;

	@Column(nullable = false)
	private int reservedPrice;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seat_id")
	private Seat seat;

	@OneToOne(mappedBy = "reservation")
	private Payment payment;

	public enum State {
		RESERVED, PAID, CANCELLED
	}

	public void setPaid() {
		this.status = State.PAID;
		this.user.setDone();
	}

	public void setCancelled() {
		this.status = State.CANCELLED;
	}

	@Builder
	public Reservation(String concertName, String concertArtist, LocalDateTime concertAt, int seatNum,
		int reservedPrice, User user, Seat seat) {
		this.concertName = concertName;
		this.concertArtist = concertArtist;
		this.concertAt = concertAt;
		this.seatNum = seatNum;
		this.reservedPrice = reservedPrice;
		this.user = user;
		this.seat = seat;
	}

	public Reservation(String concertName, String concertArtist, LocalDateTime concertAt, Seat seat) {
		this.concertName = concertName;
		this.concertArtist = concertArtist;
		this.concertAt = concertAt;
		this.seat = seat;
	}

	public static Reservation create(String concertName, String concertArtist, LocalDateTime concertAt, int seatNum,
		int reservedPrice, User user, Seat seat) {
		return new Reservation(concertName, concertArtist, concertAt, seatNum, reservedPrice, user, seat);
	}
}
