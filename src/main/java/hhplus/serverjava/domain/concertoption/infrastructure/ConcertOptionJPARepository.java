package hhplus.serverjava.domain.concertoption.infrastructure;

import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertOptionJPARepository extends JpaRepository<ConcertOption, Long> {

    List<ConcertOption> findByConcert_Id(Long concertId);
}
