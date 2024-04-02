package hhplus.serverjava.domain.reservation.entity;

import hhplus.serverjava.common.entity.BaseEntity;
import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false, updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    protected State state = State.RESERVED;

    @Column(nullable = false)
    private String concertName;

    @Column(nullable = false, length = 50)
    private String concertArtist;

    @Column(nullable = false)
    private LocalDateTime concertAt;

    @Column(nullable = false, length = 10)
    private String seatNum;

    @Column(nullable = false)
    private int reservedAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @OneToOne(mappedBy = "reservation")
    private Payment payment;

    public enum State {
        RESERVED, CANCELLED
    }
}
