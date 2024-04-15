package hhplus.serverjava.api.payment.usecase;

import hhplus.serverjava.api.payment.response.PostPayResponse;
import hhplus.serverjava.domain.payment.components.PaymentStore;
import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.reservation.components.ReservationReader;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.user.componenets.UserValidator;
import hhplus.serverjava.domain.user.entity.User;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@Server
@Transactional
@RequiredArgsConstructor
public class PaymentUseCase {

    private final PaymentStore paymentStore;
    private final UserValidator userValidator;
    private final ReservationReader reservationReader;

    public PostPayResponse execute(Long reservationId) {
        Reservation reservation = reservationReader.findReservation(reservationId);

        User user = reservation.getUser();

        // 잔액 검증
        userValidator.isValidUserPoint(reservation.getReservedPrice(), user.getPoint());

        Payment payment = Payment.builder()
                .reservation(reservation)
                .payAmount((long) reservation.getReservedPrice())
                .build();
        paymentStore.save(payment);

        // 예약 상태 PAID + 유저 상태 DONE
        reservation.setPaid();

        return new PostPayResponse(payment);
    }
}
