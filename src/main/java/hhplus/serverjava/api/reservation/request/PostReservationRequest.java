package hhplus.serverjava.api.reservation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReservationRequest {

    @NotNull
    private Long concertOptionId;

    @Future
    @NotNull
    private LocalDateTime targetDate;

    private int seatNum;

}