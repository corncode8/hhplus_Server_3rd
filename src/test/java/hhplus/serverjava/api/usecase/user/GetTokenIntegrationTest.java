package hhplus.serverjava.api.usecase.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import hhplus.serverjava.api.user.response.GetTokenResponse;
import hhplus.serverjava.api.user.usecase.GetTokenUseCase;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
// @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GetTokenIntegrationTest {
	// @checkstyle:off

	// 토큰을 생성 + 대기열 확인 통합 테스트
	@Autowired
	private UserStore userStore;
	@Autowired
	private GetTokenUseCase getTokenUseCase;

	@Container
	private static GenericContainer mySqlContainer = new MySQLContainer("mysql:8.0")
		.withReuse(true);

	// 150명 유저 생성 = 100명 -> 현재 서비스 이용중인 유저, 50명 -> 대기중인 유저
	@BeforeEach
	void setUp() {
		for (int i = 0; i < 150; i++) {
			User user = User.builder()
				.name("testUser" + i)
				.point(0L)
				.updatedAt(LocalDateTime.now())
				.build();
			if (i < 100) {
				user.setProcessing();
			}
			userStore.save(user);
		}
	}

	// 신규 유저 토큰 생성 후 대기번호 확인
	@DisplayName("신규 유저 토큰 생성 테스트")
	@Test
	void createTokenTest() {
		//given
		String testName = "new_User";

		//when
		// 토큰 생성 후 대기번호, 유저 Status return
		GetTokenResponse result = getTokenUseCase.execute(testName);

		//then
		assertNotNull(result);

		// 새로 생성된 유저는 WATING 상태
		assertEquals(User.State.WAITING, result.getState());

		// 해당 유저의 순번은 51번이어야 함.
		assertEquals(51, result.getListNum());

	}
}
