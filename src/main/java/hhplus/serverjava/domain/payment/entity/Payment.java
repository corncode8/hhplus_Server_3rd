package hhplus.serverjava.domain.payment.entity;

import hhplus.serverjava.domain.common.BaseEntity;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private Long payAmount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;


    @Builder
    public Payment(Long id, Long payAmount, Reservation reservation) {
        this.id = id;
        this.payAmount = payAmount;
        this.reservation = reservation;
    }
}
