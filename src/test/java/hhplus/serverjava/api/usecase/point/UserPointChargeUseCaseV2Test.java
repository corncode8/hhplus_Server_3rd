package hhplus.serverjava.api.usecase.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import hhplus.serverjava.api.user.response.UserPointResponse;
import hhplus.serverjava.api.user.usecase.v2.UserPointChargeUseCaseV2;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class UserPointChargeUseCaseV2Test {

	@Autowired
	private UserReader userReader;
	@Autowired
	private UserStore userStore;
	@Autowired
	private UserPointChargeUseCaseV2 userPointChargeUseCaseV2;

	@Container
	private static GenericContainer mySqlContainer = new MySQLContainer("mysql:8.0")
		.withReuse(true);
	@Container
	private static GenericContainer redisContainer = new GenericContainer(DockerImageName.parse("redis:latest"))
		.withExposedPorts(6379)
		.withReuse(true);

	@DynamicPropertySource
	static void registerPgProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.redis.host", () -> redisContainer.getHost());
		registry.add("spring.redis.port", () -> String.valueOf(redisContainer.getMappedPort(6379)));
	}

	@DisplayName("charge 테스트")
	@Test
	void chargeTest() {
		//given
		User user = User.builder()
			.name("testUser")
			.point(200L)
			.updatedAt(LocalDateTime.now())
			.build();
		userStore.save(user);

		Long chargePoint = 800L;
		//when
		UserPointResponse result = userPointChargeUseCaseV2.charge(user.getId(), chargePoint);

		//then
		User findUser = userReader.findUser(user.getId());
		assertNotNull(result);
		assertEquals(200 + 800, result.getPoint());
	}
}
