package hhplus.serverjava.api.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetWaitNumRequest {
	Long concertId;
	Long userId;
}
