package hhplus.serverjava.api.usecase.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.api.user.response.GetTokenResponse;
import hhplus.serverjava.api.user.usecase.GetTokenUseCase;
import hhplus.serverjava.api.util.jwt.JwtService;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;

@ExtendWith(MockitoExtension.class)
public class GetTokenUseCaseTest {

	@Mock
	UserReader userReader;
	@Mock
	JwtService jwtService;
	@Mock
	UserStore userStore;

	@InjectMocks
	GetTokenUseCase getTokenUseCase;

	@DisplayName("토큰 생성 API 테스트")
	@Test
	void test() {
		//given
		String jwt = "jfnherjkfera-Test-Token";

		User user = User.builder()
			.point(0L)
			.name("testUser")
			.updatedAt(LocalDateTime.now())
			.build();

		when(userStore.save(any(User.class))).thenReturn(user);
		when(jwtService.createJwt(user.getId())).thenReturn(jwt);

		//when
		GetTokenResponse result = getTokenUseCase.execute(user.getName());

		// then
		assertNotNull(result);
		assertEquals(jwt, result.getToken());

	}
}
