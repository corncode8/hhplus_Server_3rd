package hhplus.serverjava.domain.concertoption.entity;

import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.seat.entity.Seat;
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
public class ConcertOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concertOption_id", nullable = false, updatable = false)
    private Long id;

    private LocalDateTime concertAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @OneToMany(mappedBy = "concertOption")
    private List<Seat> seatList = new ArrayList<>();

    @Builder
    public ConcertOption(Long id, LocalDateTime concertAt) {
        this.id = id;
        this.concertAt = concertAt;
    }
}
