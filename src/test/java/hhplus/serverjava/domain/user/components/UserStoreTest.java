package hhplus.serverjava.domain.user.components;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserStoreRepository;

@ExtendWith(MockitoExtension.class)
public class UserStoreTest {

	@Mock
	UserStoreRepository userStoreRepository;

	@InjectMocks
	UserStore userStore;

	@DisplayName("save테스트")
	@Test
	void saveTest() {
		//given
		Long testId = 1L;
		User user = new User(testId, 500L);

		when(userStoreRepository.save(user)).thenReturn(user);

		//when
		User result = userStore.save(user);

		//then
		assertNotNull(result);
		assertEquals(result.getPoint(), user.getPoint());
	}

	//    @DisplayName("UserValidator테스트")
	//    @Test
	//    void UserValidatorTest() {
	//        //given
	//        int testNum = 0;
	//        LocalDateTime testDateTime = LocalDateTime.now().minusMinutes(15);
	//        LocalDateTime now = LocalDateTime.now();
	//
	//        List<User> userList = Arrays.asList(
	//                new User(1L, 500L, LocalDateTime.now()),
	//                new User(2L, 500L, testDateTime),
	//                new User(3L, 500L, testDateTime),
	//                new User(4L, 500L, testDateTime),
	//                new User(5L, 500L, testDateTime)
	//        );
	//
	//        //when
	//        int result = userStore.UserSchedulerValidator(userList, now, testNum);
	//
	//        //then
	//        assertNotNull(result);
	//
	//        // plusUsersNum 증가 검증
	//        assertEquals(99, result);
	//
	//        // userStatus 검증
	//        assertFalse(userList.stream().allMatch(u -> u.getStatus() == User.State.DONE));
	//
	//    }

	// 대기인원 5명 중 3명만 입장
	@DisplayName("enterService테스트")
	@Test
	void enterServiceTest() {
		//given
		int testNum = 3;
		LocalDateTime now = LocalDateTime.now();

		List<User> userList = Arrays.asList(
			new User(5L, 500L, now),
			new User(4L, 500L, now.minusMinutes(2)),
			new User(3L, 500L, now.minusMinutes(4)),
			new User(2L, 500L, now.minusMinutes(5)),
			new User(1L, 500L, now.minusMinutes(10))
		);

		//when
		userStore.enterService(userList, testNum);

		//then

		// updatedAt이 빠른 3명만 입장
		assertTrue(userList.get(4).getStatus() == User.State.PROCESSING);
		assertTrue(userList.get(3).getStatus() == User.State.PROCESSING);
		assertTrue(userList.get(2).getStatus() == User.State.PROCESSING);

		// 4,5번째는 WAITING
		assertTrue(userList.get(1).getStatus() == User.State.WAITING);
		assertTrue(userList.get(0).getStatus() == User.State.WAITING);

	}

	// 서비스를 이용중인 유저가 90명 미만일 경우 대기번호는 0번
	@DisplayName("getUserNum테스트")
	@Test
	void getUserNumTest() {
		//given
		Long testId = 90L;
		LocalDateTime now = LocalDateTime.now();
		User user = new User(testId, 500L, now);

		List<User> userList = new ArrayList<>();
		for (long i = 1; i <= 89; i++) {
			userList.add(new User(i, 500L, now.plusMinutes(i)));
		}

		//when
		Long result = userStore.getUserNum(user, userList);

		//then

		// 대기번호 0번
		assertEquals(0L, result);

		// 해당 유저 서비스 이용
		assertEquals(User.State.PROCESSING, user.getStatus());
	}

	// 대기번호 테스트
	@DisplayName("getUserWaitNum테스트")
	@Test
	void getUserWaitNumtest() {
		//given
		LocalDateTime now = LocalDateTime.now();
		List<User> userList = new ArrayList<>();
		for (long i = 1; i <= 150; i++) {
			userList.add(new User(i, 500L, now.plusMinutes(i)));
		}

		User user = new User(151L, 500L);

		//when
		Long result = userStore.getUserNum(user, userList);

		//then

		// 가장 마지막에 입장한 유저의 ID가 150번이라면 대기번호는 1번이어야 한다.
		assertEquals(1L, result);
	}

	// 대기 번호 확인
	// 서비스를 이용중인 유저가 90명보다 적을 경우 바로 processing
	public Long getUserNum(User user, List<User> userUpdAscList) {

		// updatedAt 오름차순으로 정렬
		Collections.sort(userUpdAscList, Comparator.comparing(u -> u.getUpdatedAt()));

		// 가장 마지막에 서비스에 입장한 유저
		User recentlyUpdUser = userUpdAscList.get(userUpdAscList.size() - 1);

		// 조회하려는 유저의 Id - 가장 마지막에 서비스에 입장한 유저Id = 대기번호
		Long userNum = user.getId() - recentlyUpdUser.getId();

		// 서비스를 이용중인 유저가 90명보다 적을 경우 바로 processing
		if (userUpdAscList.size() < 90) {
			user.setProcessing();
			return 0L;
		}

		return userNum;
	}

}
