package hhplus.serverjava.domain.user.entity;

import hhplus.serverjava.domain.common.entity.BaseEntity;
import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
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
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "point", nullable = false)
    private Long point;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 10)
    private State status = State.WAITING;

    @Column(nullable = false)
    private LocalDateTime updatedAt = this.getCreatedAt();

    @OneToMany(mappedBy = "user")
    private List<PointHistory> pointHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservationList = new ArrayList<>();


    public enum State {
        WAITING, PROCESSING, DONE
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public void setWaiting() {
        this.status = State.WAITING;
        this.updatedAt = LocalDateTime.now();
    }

    public void setProcessing() {
        this.status = State.PROCESSING;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDone() {
        this.status = State.DONE;
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public User(Long id, Long point) {
        this.id = id;
        this.point = point;
    }
}
