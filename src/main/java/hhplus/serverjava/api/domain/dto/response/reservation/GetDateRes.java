package hhplus.serverjava.api.domain.dto.response.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetDateRes {

    List<LocalDate> availableDates = new ArrayList<>();
    private int seatNum;
}
