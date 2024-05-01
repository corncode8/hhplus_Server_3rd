package hhplus.serverjava.api.usecase.user.mock;

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
import hhplus.serverjava.api.user.usecase.UserPointChargeUseCase;
import hhplus.serverjava.domain.user.componenets.UserPoint;
import hhplus.serverjava.domain.user.entity.User;

// @checkstyle:off
@ExtendWith(MockitoExtension.class)
public class UserPointChargeUseCaseTest {

	@Mock
	UserPoint userPoint;

	@InjectMocks
	UserPointChargeUseCase userPointChargeUseCase;

	@DisplayName("charge 테스트")
	@Test
	void chargeTest() {
		//given
		Long testId = 1L;
		User user = new User(testId, 5000L);
		Long chargePoint = 5000L;

		User newUser = new User(testId + 1L, user.getPoint() + chargePoint);

		UserPointResponse testResponse = new UserPointResponse(newUser.getId(), user.getPoint() + chargePoint);

		when(userPoint.pointCharge(testId, chargePoint)).thenReturn(newUser);

		//when
		UserPointResponse result = userPointChargeUseCase.charge(testId, chargePoint);

		//then
		assertNotNull(result);
		assertEquals(result.getUserId(), testResponse.getUserId());
		assertEquals(result.getPoint(), testResponse.getPoint());
	}
}
