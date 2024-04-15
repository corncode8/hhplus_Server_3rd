package hhplus.serverjava.api.payment.response;

import hhplus.serverjava.domain.payment.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostPayResponse {

    private Long payId;

    private Long reservationId;

    private Long payAmount;
    private LocalDateTime payAt;

    public PostPayResponse(Long payId, Long reservationId, Long payAmount, LocalDateTime payAt) {
        this.payId = payId;
        this.reservationId = reservationId;
        this.payAmount = payAmount;
        this.payAt = payAt;
    }

    public PostPayResponse(Payment payment) {
        this.payId = payment.getId();
        this.reservationId = payment.getReservation().getId();
        this.payAmount = payment.getPayAmount();
        this.payAt = payment.getCreatedAt();
    }
}
