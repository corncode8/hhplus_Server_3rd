package hhplus.serverjava.domain.queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

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

import hhplus.serverjava.domain.queue.infrastructure.RedisQueueCoreRepository;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class RedisQueueCoreRepositoryTest {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private ZSetOperations<String, String> zSetOperations;

	@Autowired
	private RedisQueueCoreRepository redisQueueCoreRepository;

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

	private final String WAITING_KEY = "waiting:concert:";
	private final String WORKING_KEY = "working:concert:";

	@BeforeEach
	void setUp() {
		zSetOperations = redisTemplate.opsForZSet();
	}

	@DisplayName("addQueue 테스트")
	@Test
	void addQueueTest() {
		//given
		Long concertId = 1L;
		Long userId = 1L;

		//when
		Long result = redisQueueCoreRepository.addWaitingQueue(concertId, userId);

		//then
		assertNotNull(result);

		// 첫 유저의 대기번호 = 0번
		assertEquals(0L, result);
	}

	@DisplayName("findUserRank 테스트")
	@Test
	void findUserRankTest() {
		//given
		Long concertId = 1L;

		// userId 1 ~ 10 까지 대기열에 추가
		for (int i = 0; i < 10; i++) {
			Long addNum = redisQueueCoreRepository.addWaitingQueue(concertId, i + 1L);
			System.out.println("addNum = " + addNum);
		}

		//when
		Long result = redisQueueCoreRepository.findUserRank(concertId, 10L);

		// 대기열에 없는 userId
		Long nonExistUser = redisQueueCoreRepository.findUserRank(concertId, 50L);

		//then
		assertNotNull(result);
		assertNotNull(nonExistUser);

		// 10번 유저 순번 -> 9번
		assertEquals(9, result);

		// 대기열에 없는 유저 -> return -1
		assertEquals(-1, nonExistUser);
	}

	@DisplayName("popWaitingQueue 테스트")
	@Test
	void popWaitingQueue() {
		//given
		Long concertId = 1L;
		long enterNum = 20;

		String key = WAITING_KEY + concertId;
		for (int i = 0; i < 100; i++) {
			zSetOperations.add(key, String.valueOf(i + 1), System.currentTimeMillis());
		}
		Long beforeSize = zSetOperations.size(key);

		//when
		List<String> result = redisQueueCoreRepository.popWaitingQueue(concertId, enterNum);

		//then
		assertNotNull(result);

		// 초기 Queue size
		assertEquals(100, beforeSize);
		// 20명 빠져나간 후 size
		assertEquals(20, result.size());

		// 21번 유저의 rank = 0번
		Long rank = zSetOperations.rank(key, String.valueOf(21L));
		assertEquals(0, rank);
	}

	@DisplayName("addWorkingQueue 테스트")
	@Test
	void addWorkingQueueTest() {
		//given
		Long concertId = 1L;
		Long userId = 1L;

		//when
		Long result = redisQueueCoreRepository.addWorkingQueue(concertId, userId);

		//then
		assertNotNull(result);
		assertEquals(result, userId);
	}

	@DisplayName("workingUserNum 테스트")
	@Test
	void workingUserNumTest() {
		//given
		Long concertId = 1L;
		String key = "working:concert:" + concertId;

		for (int i = 0; i < 20; i++) {
			zSetOperations.add(key, String.valueOf(i + 1L), System.currentTimeMillis());
		}

		//when
		Long result = redisQueueCoreRepository.workingUserNum(concertId);

		//then
		assertNotNull(result);
		assertEquals(20, result);
	}

	@DisplayName("popWoringQueue 테스트")
	@Test
	void popWoringQueueTest() {
		//given
		Long concertId = 1L;
		String key = "working:concert:" + concertId;

		for (int i = 0; i < 20; i++) {
			zSetOperations.add(key, String.valueOf(i + 1L), System.currentTimeMillis());
		}

		//when
		redisQueueCoreRepository.popWoringQueue(concertId, 15L);
		redisQueueCoreRepository.popWoringQueue(concertId, 5L);

		//then
		assertEquals(18, zSetOperations.size(key));
	}
}
