package hhplus.serverjava.api.payment.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.payment.request.PostPayRequest;
import hhplus.serverjava.api.payment.response.PostPayResponse;
import hhplus.serverjava.api.reservation.usecase.ReservationStatusUpdateUseCase;
import hhplus.serverjava.domain.eventhistory.event.EventHistorySave;
import hhplus.serverjava.domain.payment.PaymentEventPublisher;
import hhplus.serverjava.domain.payment.components.PaymentCreator;
import hhplus.serverjava.domain.payment.components.PaymentStore;
import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.queue.components.RedisQueueManager;
import hhplus.serverjava.domain.reservation.components.ReservationReader;
import hhplus.serverjava.domain.reservation.components.ReservationValidator;
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
	private final ReservationValidator reservationValidator;
	private final ReservationStatusUpdateUseCase reservationStatusUpdateUseCase;

	public PostPayResponse pay(PostPayRequest request, Long userId) {

		User user = userReader.findUser(userId);

		Reservation reservation = reservationReader.findReservation(request.getReservationId());

		// 임시 배정된 좌석 존재 여부 + 만료되었는지 확인
		reservationValidator.validate(reservation);

		// 유저 포인트 차감
		userValidator.isValidUserPoint(request.getPayAmount(), user);

		// 결제
		Payment payment = PaymentCreator.create((long)request.getPayAmount(), reservation);
		paymentStore.save(payment);

		// 예약 상태 변경
		reservationStatusUpdateUseCase.setPaid(reservation);

		redisQueueManager.popFromWorkingQueue(request.getConcertId(), user.getId());

		// 예약 성공 이벤트 발행
		eventPublisher.success(new EventHistorySave(payment.getId()));

		return new PostPayResponse(payment);
	}
}
