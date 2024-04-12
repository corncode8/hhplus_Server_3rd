package hhplus.serverjava.api.payment.usecase;

import hhplus.serverjava.api.payment.response.PostPayResponse;
import hhplus.serverjava.domain.payment.components.PaymentStore;
import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.reservation.components.ReservationReader;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@Server
@Transactional
@RequiredArgsConstructor
public class PaymentUseCase {

    private final PaymentStore paymentStore;
    private final ReservationReader reservationReader;

    public PostPayResponse execute(Long reservationId) {
        Reservation reservation = reservationReader.findReservation(reservationId);

        Payment payment = Payment.builder()
                .reservation(reservation)
                .payAmount((long) reservation.getReservedPrice())
                .build();
        paymentStore.save(payment);

        return new PostPayResponse();
    }
}
