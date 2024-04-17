package hhplus.serverjava.api.payment.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPayRequest {

    private Long reservationId;
    @NotNull
    private int payAmount;
}
