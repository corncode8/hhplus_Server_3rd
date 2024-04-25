package hhplus.serverjava.api.usecase.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.api.user.response.GetUserResponse;
import hhplus.serverjava.api.user.usecase.GetWaitNumUseCase;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;

@ExtendWith(MockitoExtension.class)
public class GetWaitNumUseCaseTest {

	@Mock
	UserReader userReader;
	@Mock
	UserStore userStore;

	@InjectMocks
	GetWaitNumUseCase getWaitNumUseCase;

	@DisplayName("대기열 확인 API 테스트")
	@Test
	void test() {
		//given
		Long userId = 105L;
		User testUser = new User(userId, 1000L);

		List<User> mockUser = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Long idx = i + 1L;
			Long point = 500L;

			User user = new User(idx, point);
			user.setProcessing();

			mockUser.add(user);
		}

		when(userReader.findUser(userId)).thenReturn(testUser);
		when(userReader.findUsersByStatus(User.State.PROCESSING)).thenReturn(mockUser);

		//when
		GetUserResponse result = getWaitNumUseCase.execute(userId);

		//then
		assertNotNull(result);
		assertEquals(userId - mockUser.size(), 5);

	}

}
