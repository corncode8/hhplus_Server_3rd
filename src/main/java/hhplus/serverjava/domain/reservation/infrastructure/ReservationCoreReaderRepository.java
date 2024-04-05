package hhplus.serverjava.domain.reservation.infrastructure;

import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.reservation.repository.ReservationReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationCoreReaderRepository implements ReservationReaderRepository {

    private final ReservationJPARepository repository;
    @Override
    public List<Reservation> findExpiredReservaions(LocalDateTime now) {
        return repository.findExpiredReservations(now);
    }
}
