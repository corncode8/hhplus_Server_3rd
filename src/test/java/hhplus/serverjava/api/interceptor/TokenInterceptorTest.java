package hhplus.serverjava.api.interceptor;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import hhplus.serverjava.api.user.response.GetTokenResponse;
import hhplus.serverjava.api.user.usecase.GetTokenUseCase;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TokenInterceptorTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserStore userStore;
	@Autowired
	private GetTokenUseCase getTokenUseCase;

	private static MySQLContainer mySqlContainer = new MySQLContainer("mysql:8");

	@AfterEach
	void tearDown() {
		mySqlContainer.stop();
	}

	// 180명 유저 생성 = 100명 -> 서비스 이용중 유저, 80명 -> 대기중 유저
	@BeforeEach
	void setUp() {
		mySqlContainer.start();
		for (int i = 0; i < 180; i++) {
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

	// 토큰 생성하고 해당 유저로 인터셉터 테스트
	@SuppressWarnings("checkstyle:RegexpSingleline")
	@DisplayName("TokenInterceptor 테스트")
	@Test
	void interceptorTest() throws Exception {
		//given

		// 유저 새로 생성
		GetTokenResponse helloWorldUser = getTokenUseCase.execute("HelloWorld");

		// 새로 생성된 유저의 토큰을 헤더로 설정
		mockMvc.perform(get("/api/wait/check")
				.header("Authorization", helloWorldUser.getToken()))
			.andExpect(status().isOk())
			.andExpect(request().attribute("userId", notNullValue()))
			.andExpect(request().attribute("waitNum", helloWorldUser.getListNum()));

		assertEquals(81, helloWorldUser.getListNum());
	}
}
