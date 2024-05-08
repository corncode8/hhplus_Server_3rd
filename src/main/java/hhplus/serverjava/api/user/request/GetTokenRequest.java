package hhplus.serverjava.api.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetTokenRequest {

	String username;
	Long concertId;
}
