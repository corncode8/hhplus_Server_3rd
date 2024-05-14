package hhplus.serverjava.api.payment.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.payment.request.PostPayRequest;
import hhplus.serverjava.api.payment.response.PostPayResponse;
import hhplus.serverjava.domain.payment.components.PaymentCreator;
import hhplus.serverjava.domain.payment.components.PaymentStore;
import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.payment.event.PaymentEventPublisher;
import hhplus.serverjava.domain.payment.event.PaymentSuccessEvent;
import hhplus.serverjava.domain.queue.components.RedisQueueManager;
import hhplus.serverjava.domain.reservation.components.ReservationReader;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserValidator;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentUseCase {

	private final UserReader userReader;
	private final PaymentStore paymentStore;
	private final UserValidator userValidator;
	private final ReservationReader reservationReader;
	private final RedisQueueManager redisQueueManager;
	private final PaymentEventPublisher eventPublisher;

	public PostPayResponse pay(PostPayRequest request, Long userId) {
		Reservation reservation = reservationReader.findReservation(request.getReservationId());

		User user = userReader.findUser(userId);

		// 임시 배정된 좌석 존재 여부 + 만료되었는지 확인

		// 유저 포인트 차감
		userValidator.isValidUserPoint(request.getPayAmount(), user);

		// 결제
		Payment payment = PaymentCreator.create((long)request.getPayAmount(), reservation);
		paymentStore.save(payment);

		redisQueueManager.popFromWorkingQueue(request.getConcertId(), user.getId());

		// 예약 상태 변경, 예약 정보 전송
		eventPublisher.success(new PaymentSuccessEvent(reservation.getId(), payment.getId()));

		return new PostPayResponse(payment);
	}
}
