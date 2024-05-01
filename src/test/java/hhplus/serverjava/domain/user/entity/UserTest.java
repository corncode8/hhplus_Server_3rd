package hhplus.serverjava.domain.user.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserTest {
	@DisplayName("setWaiting테스트")
	@Test
	void setWaitingTest() {
		//given
		Long testId = 1L;
		User user = new User(testId, 500L);

		//when
		LocalDateTime testDateTime = LocalDateTime.now();
		user.setWaiting();

		//then
		assertEquals(User.State.WAITING, user.getStatus());
		assertTrue(Math.abs(Duration.between(testDateTime, user.getUpdatedAt()).getSeconds()) < 5);
	}

	@DisplayName("setProcessing테스트")
	@Test
	void setProcessingTest() {
		//given
		Long testId = 1L;
		User user = new User(testId, 500L);

		//when
		LocalDateTime testDateTime = LocalDateTime.now();
		user.setProcessing();

		//then
		assertEquals(User.State.PROCESSING, user.getStatus());
		assertTrue(Math.abs(Duration.between(testDateTime, user.getUpdatedAt()).getSeconds()) < 5);
	}

	@DisplayName("setDone테스트")
	@Test
	void setDoneTest() {
		//given
		Long testId = 1L;
		User user = new User(testId, 500L);

		//when
		LocalDateTime testDateTime = LocalDateTime.now();
		user.setDone();

		//then
		assertEquals(User.State.DONE, user.getStatus());
		assertTrue(Math.abs(Duration.between(testDateTime, user.getUpdatedAt()).getSeconds()) < 5);
	}

	@DisplayName("sumPoint테스트")
	@Test
	void sumPointTest() {
		//given
		Long testId = 1L;
		User user = new User(testId, 500L);

		//when
		Long testPoint = 500L;
		user.sumPoint(testPoint);

		//then
		assertEquals(500 + 500, user.getPoint());
	}

	@DisplayName("usePoint테스트")
	@Test
	void usePointTest() {
		//given
		Long testId = 1L;
		User user = new User(testId, 50000L);

		//when
		Long testPoint = 5000L;
		user.usePoint(testPoint);

		//then
		assertEquals(50000 - 5000, user.getPoint());
	}
}
