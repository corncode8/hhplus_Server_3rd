package hhplus.serverjava.domain.seat.infrastructure;

import hhplus.serverjava.domain.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;

public interface SeatJPARepository extends JpaRepository<Seat, Long> {

    @Query("select s from Seat s where s.concertOption.concert.id = :concertId " +
            "and s.concertOption.concertAt = :targetDate " +
            "and s.state = :state")
    List<Seat> findAvailableSeats(@Param("concertId") Long concertId, @Param("targetDate") LocalDateTime targetDate,
                                  @Param("state")Seat.State state);
    @Query("select s from Seat s where s.concertOption.id = :concertOptionId " +
            "and s.concertOption.concertAt = :targetDate " +
            "and s.state = :state " +
            "and s.seatNum = :seatNum")
    Seat findAvailableSeat(@Param("concertOptionId") Long concertOptionId, @Param("targetDate")LocalDateTime targetDate,
                                   @Param("state")Seat.State state, @Param("seatNum")int seatNum);

}
