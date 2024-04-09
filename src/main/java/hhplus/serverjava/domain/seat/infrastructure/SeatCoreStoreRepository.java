package hhplus.serverjava.domain.seat.infrastructure;

import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.seat.repository.SeatStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SeatCoreStoreRepository implements SeatStoreRepository {

    private final SeatJPARepository seatJPARepository;
    @Override
    public Seat save(Seat seat) {
        return seatJPARepository.save(seat);
    }
}
