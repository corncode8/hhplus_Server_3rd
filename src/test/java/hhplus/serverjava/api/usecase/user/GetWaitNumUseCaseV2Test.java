package hhplus.serverjava.api.usecase.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import hhplus.serverjava.api.user.request.GetWaitNumRequest;
import hhplus.serverjava.api.user.response.GetUserResponse;
import hhplus.serverjava.api.user.usecase.GetWaitNumUseCaseV2;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class GetWaitNumUseCaseV2Test {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private ZSetOperations<String, String> zSetOperations;

	@Autowired
	private GetWaitNumUseCaseV2 getWaitNumUseCaseV2;

	@Container
	private static GenericContainer mySqlContainer = new MySQLContainer("mysql:8.0")
		.withReuse(true);
	@Container
	private static GenericContainer redisContainer = new GenericContainer(DockerImageName.parse("redis:latest"))
		.withExposedPorts(6379);

	@DynamicPropertySource
	static void registerPgProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.redis.host", () -> redisContainer.getHost());
		registry.add("spring.redis.port", () -> String.valueOf(redisContainer.getMappedPort(6379)));
	}

	@BeforeEach
	void setUp() {
		zSetOperations = redisTemplate.opsForZSet();
	}

	private final String WAITING_KEY = "waiting:concert:";
	private final String WORKING_KEY = "working:concert:";

	@DisplayName("대기번호 return 테스트")
	@Test
	void executeTest() {
		//given
		Long concertId = 1L;
		Long userId = 1L;
		String key = WAITING_KEY + concertId;

		GetWaitNumRequest request = new GetWaitNumRequest(concertId, userId);

		zSetOperations.add(key, userId.toString(), System.currentTimeMillis());

		//when
		GetUserResponse result = getWaitNumUseCaseV2.execute(request);

		//then
		assertNotNull(result);
		assertEquals(0, result.getListNum());
	}
}
