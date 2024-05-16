package hhplus.serverjava.api.reservation.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.domain.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationPaidUpdateUseCase {

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void setPaid(Reservation reservation) {
		// 예약 상태 변경 + 유저 상태 변경
		reservation.setPaid();
	}
}
