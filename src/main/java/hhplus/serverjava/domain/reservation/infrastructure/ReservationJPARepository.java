package hhplus.serverjava.domain.reservation.infrastructure;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hhplus.serverjava.domain.reservation.entity.Reservation;

public interface ReservationJPARepository extends JpaRepository<Reservation, Long> {

	@Query("select r from Reservation r where r.seat.expiredAt < :now and r.status = 'RESERVED'")
	List<Reservation> findExpiredReservations(@Param("now") LocalDateTime now);

}
