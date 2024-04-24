package hhplus.serverjava.api.payment.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.payment.request.PostPayRequest;
import hhplus.serverjava.api.payment.response.PostPayResponse;
import hhplus.serverjava.domain.payment.components.PaymentStore;
import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.reservation.components.ReservationReader;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserValidator;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentUseCase {

	private final UserReader userReader;
	private final PaymentStore paymentStore;
	private final UserValidator userValidator;
	private final ReservationReader reservationReader;

	public PostPayResponse execute(PostPayRequest request, Long userId) {
		Reservation reservation = reservationReader.findReservation(request.getReservationId());

		User jwtUser = userReader.findUser(userId);

		User user = reservation.getUser();

		userValidator.validUser(jwtUser, user);

		// 잔액 검증
		userValidator.isValidUserPoint(request.getPayAmount(), user.getPoint());

		Payment payment = Payment.builder()
			.reservation(reservation)
			.payAmount((long)reservation.getReservedPrice())
			.build();
		paymentStore.save(payment);

		// 예약 상태 PAID + 유저 상태 DONE
		reservation.setPaid();

		return new PostPayResponse(payment);
	}
}
