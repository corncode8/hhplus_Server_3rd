package hhplus.serverjava.domain.seat.entity;

import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int seatNum;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 10)
    private State status = State.AVAILABLE;

    private LocalDateTime expiredAt;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concertOption_id")
    private ConcertOption concertOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @OneToMany(mappedBy = "seat")
    private List<Reservation> reservationList = new ArrayList<>();

    public enum State {
        AVAILABLE, RESERVED
    }

    @Builder
    public Seat(Long id, int seatNum, int price, ConcertOption concertOption, Concert concert) {
        this.id = id;
        this.seatNum = seatNum;
        this.price = price;
        this.concertOption = concertOption;
        this.concert = concert;
    }

    public Seat(Long id, int seatNum, int price) {
        this.id = id;
        this.seatNum = seatNum;
        this.price = price;
    }

    public void setReserved() {
        this.status = State.RESERVED;
        this.expiredAt = LocalDateTime.now().plusMinutes(5);
    }
    public void setAvailable() {
        this.status = State.AVAILABLE;
    }

}
