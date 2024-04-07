package hhplus.serverjava.domain.concert.infrastructure;

import hhplus.serverjava.domain.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertJPARepository extends JpaRepository<Concert, Long> {

    List<Concert> findConcertByState(Concert.State state);
}
