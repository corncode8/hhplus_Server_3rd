package hhplus.serverjava.domain.concert.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hhplus.serverjava.domain.concert.entity.Concert;

public interface ConcertJPARepository extends JpaRepository<Concert, Long> {

	List<Concert> findConcertsByStatus(Concert.State state);
}
