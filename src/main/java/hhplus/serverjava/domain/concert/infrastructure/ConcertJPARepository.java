package hhplus.serverjava.domain.concert.infrastructure;

import hhplus.serverjava.domain.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJPARepository extends JpaRepository<Concert, Long> {
}
