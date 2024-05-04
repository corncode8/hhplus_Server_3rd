package hhplus.serverjava.api.payment.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.payment.request.PostPayRequest;
import hhplus.serverjava.api.payment.response.PostPayResponse;
import hhplus.serverjava.domain.payment.components.PaymentCreator;
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

		User user = userReader.findUser(userId);

		// 임시 배정된 좌석 존재 여부 + 만료되었는지 확인

		// 잔액 검증
		userValidator.isValidUserPoint(request.getPayAmount(), user.getPoint());

		// 결제
		Payment payment = PaymentCreator.create((long)request.getPayAmount(), reservation);
		paymentStore.save(payment);
		// 유저 포인트 차감
		user.usePoint((long)request.getPayAmount());

		// 예약 완료 처리 + 유저 상태 DONE
		reservation.setPaid();

		return new PostPayResponse(payment);
	}
}
