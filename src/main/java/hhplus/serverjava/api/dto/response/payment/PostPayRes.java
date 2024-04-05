package hhplus.serverjava.api.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPayRes {

    private Long id;

    private Long reservationId;

    private int payAmount;
    private LocalDateTime payAt;
}
