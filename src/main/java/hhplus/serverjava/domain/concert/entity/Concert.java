package hhplus.serverjava.domain.concert.entity;

import hhplus.serverjava.domain.common.entity.BaseEntity;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.seat.entity.Seat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Column(nullable = false, length = 50)
    private String artist;

    private State status = State.SHOWING;

    @OneToMany(mappedBy = "concert")
    private List<ConcertOption> concertOption = new ArrayList<>();

    @OneToMany(mappedBy = "concert")
    private List<Seat> seatList = new ArrayList<>();

    @Builder
    public Concert(Long id, String name, String artist) {
        this.id = id;
        this.name = name;
        this.artist = artist;
    }

    public enum State {
        SHOWING, ISOVER
    }

    public void addSeatList(Seat seat) {
        this.seatList.add(seat);
    }
}
