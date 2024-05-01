package hhplus.serverjava.api.usecase.point.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.api.user.response.UserPointResponse;
import hhplus.serverjava.api.user.usecase.GetUserPointUseCase;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.entity.User;

@ExtendWith(MockitoExtension.class)
public class GetUserPointResponseUseCaseTest {

	@Mock
	UserReader userReader;

	@InjectMocks
	GetUserPointUseCase getUserPointUseCase;

	@DisplayName("유저 포인트 조회 테스트")
	@Test
	void test() {
		//given
		Long userId = 1L;
		Long point = 500L;
		User user = new User(userId, point);

		when(userReader.findUser(userId)).thenReturn(user);

		//when
		UserPointResponse result = getUserPointUseCase.execute(userId);

		//then
		assertNotNull(result);
		assertEquals(point, result.getPoint());
	}
}
