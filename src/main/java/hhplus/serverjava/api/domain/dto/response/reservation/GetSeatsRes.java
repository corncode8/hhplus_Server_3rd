package hhplus.serverjava.api.domain.dto.response.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetSeatsRes {

    // Mock API용 List
    List<String> availableSeatsList = new ArrayList<>();
}
