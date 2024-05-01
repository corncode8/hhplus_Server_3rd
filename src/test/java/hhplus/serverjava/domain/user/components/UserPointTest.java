package hhplus.serverjava.domain.user.components;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_ENOUGH_POINT;
import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FIND_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.user.componenets.UserPoint;
import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserReaderRepository;

@ExtendWith(MockitoExtension.class)
public class UserPointTest {

	@Mock
	UserReaderRepository userReaderRepository;
	@InjectMocks
	UserPoint userPoint;

	@DisplayName("pointCharge 테스트")
	@Test
	void pointChargeTest() {
		//given
		Long testId = 1L;
		User user = new User(testId, 500L);
		Long chargePoint = 50000L;

		when(userReaderRepository.findById(testId)).thenReturn(Optional.of(user));

		//when
		User result = userPoint.pointCharge(testId, chargePoint);

		//then
		assertNotNull(result);
		assertEquals(500 + 50000, user.getPoint());
	}

	@DisplayName("pointCharge_NotFound_테스트")
	@Test
	void pointChargeNotFoundTest() {
		//given
		Long testId = 1L;
		Long chargePoint = 50000L;

		//when & then
		BaseException exception = assertThrows(BaseException.class, () -> userPoint.pointCharge(testId, chargePoint));
		assertEquals(NOT_FIND_USER.getMessage(), exception.getMessage());
	}

	@DisplayName("pointUse 테스트")
	@Test
	void pointUseTest() {
		//given
		Long testId = 1L;
		User user = new User(testId, 50000L);
		Long chargePoint = 10000L;

		when(userReaderRepository.findById(testId)).thenReturn(Optional.of(user));

		//when
		User result = userPoint.pointUse(testId, chargePoint);

		//then
		assertNotNull(result);
		assertEquals(50000 - 10000, user.getPoint());
	}

	@DisplayName("pointUse 잔액부족 테스트")
	@Test
	void pointUseFailTest() {
		//given
		Long testId = 1L;
		User user = new User(testId, 5000L);
		Long chargePoint = 10000L;

		when(userReaderRepository.findById(testId)).thenReturn(Optional.of(user));

		//when & then
		BaseException exception = assertThrows(BaseException.class, () -> userPoint.pointUse(testId, chargePoint));
		assertEquals(NOT_ENOUGH_POINT.getMessage(), exception.getMessage());
	}

	@DisplayName("pointUse_NotFound_테스트")
	@Test
	void pointUseNotFoundTest() {
		//given
		Long testId = 1L;
		Long chargePoint = 50000L;

		//when & then
		BaseException exception = assertThrows(BaseException.class, () -> userPoint.pointUse(testId, chargePoint));
		assertEquals(NOT_FIND_USER.getMessage(), exception.getMessage());
	}

	@DisplayName("getPoint 테스트")
	@Test
	void getPointTest() {
		//given
		Long testId = 1L;
		User user = new User(testId, 50000L);

		when(userReaderRepository.findById(testId)).thenReturn(Optional.of(user));

		//when
		Long result = userPoint.getPoint(testId);

		//then
		assertNotNull(result);
		assertEquals(50000, user.getPoint());
	}

	@DisplayName("getPoint_NotFound_테스트")
	@Test
	void getPointNotFoundTest() {
		//given
		Long testId = 1L;

		//when & then
		BaseException exception = assertThrows(BaseException.class, () -> userPoint.getPoint(testId));
		assertEquals(NOT_FIND_USER.getMessage(), exception.getMessage());
	}
}
