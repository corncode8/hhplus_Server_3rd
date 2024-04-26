package hhplus.serverjava.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import hhplus.serverjava.api.user.request.PatchUserRequest;
import hhplus.serverjava.api.util.jwt.JwtService;
import hhplus.serverjava.domain.pointhistory.components.PointHistoryStore;
import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserStore userStore;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private PointHistoryStore pointHistoryStore;

	private static MySQLContainer mySqlContainer = new MySQLContainer("mysql:8");

	@BeforeEach
	void setUp() {
		mySqlContainer.start();
	}

	@AfterEach
	void tearDown() {
		mySqlContainer.stop();
	}

	@DisplayName("토큰 발급 테스트")
	@Test
	void getTokenTest() throws Exception {
		//given
		String newUsername = "testUser";

		mockMvc.perform(get("/api/wait")
				.accept(MediaType.APPLICATION_JSON)
				.param("username", newUsername))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))

			.andExpect(jsonPath("$.result.token").isNotEmpty())
			.andExpect(jsonPath("$.result.listNum").value(0))
			.andExpect(jsonPath("$.result.state").value("PROCESSING"));
	}

	@DisplayName("대기열 확인 테스트")
	@Test
	void checkQueueTest() throws Exception {
		//given
		User user = User.builder()
			.name("testUser")
			.point(5000000L)
			.updatedAt(LocalDateTime.now())
			.build();
		userStore.save(user);

		String jwt = jwtService.createJwt(user.getId());

		mockMvc.perform(get("/api/wait/check")
				.header("Authorization", jwt)
				.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))

			.andExpect(jsonPath("$.result.listNum").value(0));
	}

	@DisplayName("잔액 충전 테스트")
	@Test
	void chargePointTest() throws Exception {
		//given
		User user = User.builder()
			.name("testUser")
			.point(50L)
			.updatedAt(LocalDateTime.now())
			.build();
		userStore.save(user);

		Long chargePoint = 50000L;
		PatchUserRequest request = new PatchUserRequest(chargePoint);

		mockMvc.perform(patch("/api/point/{userId}/charge", user.getId())
				.contentType(MediaType.APPLICATION_JSON)  // 콘텐츠 타입 추가
				.content(new ObjectMapper().writeValueAsString(request))
				.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))

			// 유저 ID, Point 검증
			.andExpect(jsonPath("$.result.point").value(user.getPoint() + chargePoint))
			.andExpect(jsonPath("$.result.userId").value(user.getId()));
	}

	@DisplayName("잔액 조회 테스트")
	@Test
	void getPointTest() throws Exception {
		//given
		User user = User.builder()
			.name("testUser")
			.point(5000L)
			.updatedAt(LocalDateTime.now())
			.build();
		userStore.save(user);

		mockMvc.perform(get("/api/point/{userId}/account", user.getId())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))

			.andExpect(jsonPath("$.result.point").value(5000));
	}

	@DisplayName("잔액 리스트 조회 테스트")
	@Test
	void pointHistoryTest() throws Exception {
		//given
		User user = User.builder()
			.name("testUser")
			.point(0L)
			.updatedAt(LocalDateTime.now())
			.build();
		userStore.save(user);

		int historyListSize = 5;
		for (int i = 0; i < historyListSize; i++) {
			PointHistory pointHistory = new PointHistory(i + 1L, user, PointHistory.State.CHARGE, 500L + (i + 1));
			pointHistoryStore.save(pointHistory);
		}

		mockMvc.perform(get("/api/point/{userId}/histories", user.getId())
				.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))

			.andExpect(jsonPath("$.result.pointHistoryList.size()").value(historyListSize));
	}
}
