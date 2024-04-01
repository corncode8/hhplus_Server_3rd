package hhplus.serverjava.domain.concert.entity;

import hhplus.serverjava.common.entity.BaseEntity;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Concert extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int seatsNum;

    @Column(columnDefinition = "TIMESTAMP", name = "endedAt")
    private LocalDateTime endedAt;

    @OneToMany(mappedBy = "concert")
    private List<Seat> seatList = new ArrayList<>();

}
