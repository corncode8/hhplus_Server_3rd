package hhplus.serverjava.domain.concertoption.entity;

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
public class ConcertOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concertOption_id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime endedAt;

    @Column(nullable = false)
    private int seatsNum;

    @OneToMany(mappedBy = "concertOption")
    private List<Seat> seatList = new ArrayList<>();

}
