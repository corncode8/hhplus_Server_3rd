package hhplus.serverjava.domain.reservation.infrastructure;

import hhplus.serverjava.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationJPARepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r where r.seat.expiredAt < :now and r.state = 'RESERVED'")
    List<Reservation> findExpiredReservations(@Param("now")LocalDateTime now);

}
