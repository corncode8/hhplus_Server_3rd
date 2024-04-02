package hhplus.serverjava.domain.user.entity;

import hhplus.serverjava.common.entity.BaseEntity;
import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "point", nullable = false)
    private Long point;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status = Status.WAITING;

    @OneToMany(mappedBy = "user")
    private List<PointHistory> pointHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservationList = new ArrayList<>();


    public enum Status {
        WAITING, PROCESSING, DONE
    }
}