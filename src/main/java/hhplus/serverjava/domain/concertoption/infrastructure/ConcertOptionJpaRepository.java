package hhplus.serverjava.domain.concertoption.infrastructure;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;

public interface ConcertOptionJpaRepository extends JpaRepository<ConcertOption, Long> {
	List<ConcertOption> findByConcert_Id(Long concertId);

	@Query("select c.concert from ConcertOption c where c.id = :concertOptionId")
	Optional<Concert> findConcert(@Param("concertOptionId") Long concertOptionId);

	Optional<ConcertOption> findByConcert_IdAndConcertAt(Long concertId, LocalDateTime concertAt);

}
