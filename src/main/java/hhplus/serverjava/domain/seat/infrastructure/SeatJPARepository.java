package hhplus.serverjava.domain.seat.infrastructure;

import hhplus.serverjava.domain.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SeatJPARepository extends JpaRepository<Seat, Long> {

    @Query("select s from Seat s where s.concertOption.concert.id = :concertId " +
            "and :targetDate BETWEEN s.concertOption.startedAt and s.concertOption.endedAt " +
            "and s.state = :state")
    List<Seat> findAvailableSeats(@Param("concertId") Long concertId, @Param("targetDate")LocalDate targetDate,
                                  @Param("state")Seat.State state);
}
