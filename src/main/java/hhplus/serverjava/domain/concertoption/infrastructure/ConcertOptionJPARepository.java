package hhplus.serverjava.domain.concertoption.infrastructure;

import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertOptionJPARepository extends JpaRepository<ConcertOption, Long> {

    ConcertOption findByConcert_Id(Long concertId);
}
