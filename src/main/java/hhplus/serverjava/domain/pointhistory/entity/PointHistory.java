package hhplus.serverjava.domain.pointhistory.entity;

import hhplus.serverjava.common.entity.BaseEntity;
import hhplus.serverjava.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pointhistory_id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    protected State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public enum State{
        CHARGE, USE
    }

    @Builder
    public PointHistory(Long id, User user, State state, Long amount) {
        this.id = id;
        this.user = user;
        this.state = state;
        this.amount = amount;
    }
}
