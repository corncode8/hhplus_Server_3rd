package hhplus.serverjava.domain.queue.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

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
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class RedisQueueManagerTest {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private ZSetOperations<String, String> zSetOperations;

	@Autowired
	private RedisQueueManager redisQueueManager;

	@Autowired
	private RedisQueueCoreRepository redisQueueCoreRepository;

	@Autowired
	private UserStore userStore;

	@Autowired
	private UserReader userReader;

	@Autowired
	EntityManager em;

	@Container
	private static GenericContainer mySqlContainer = new MySQLContainer("mysql:8.0")
		.withReuse(true);
	@Container
	private static GenericContainer redisContainer = new GenericContainer(DockerImageName.parse("redis:latest"))
		.withExposedPorts(6379);

	private final String WAITING_KEY = "waiting:concert:";
	private final String WORKING_KEY = "working:concert:";

	@DynamicPropertySource
	static void registerPgProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.redis.host", () -> redisContainer.getHost());
		registry.add("spring.redis.port", () -> String.valueOf(redisContainer.getMappedPort(6379)));
	}

	@BeforeEach
	void setUp() {
		zSetOperations = redisTemplate.opsForZSet();
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
		Long result = redisQueueManager.findUserRank(concertId, 10L);

		// 대기열에 없는 userId
		Long nonExistUser = redisQueueManager.findUserRank(concertId, 20L);

		//then
		assertNotNull(result);
		assertNotNull(nonExistUser);

		// 10번 유저 순번 -> 9번
		assertEquals(9, result);

		// 대기열에 없는 유저 -> return -1
		assertEquals(-1, nonExistUser);
	}

	@DisplayName("addQueue 테스트")
	@Test
	void addQueueTest() {
		//given
		Long concertId = 1L;
		String key = "waiting:concert:" + concertId;

		//when
		for (int i = 0; i < 10; i++) {
			redisQueueManager.addQueue(concertId, i + 1L);
		}
		Long size = zSetOperations.size(key);

		//then
		assertNotNull(size);
		assertEquals(10, size);
	}

	@DisplayName("popUserFromQueue 테스트")
	@Test
	void popUserFromQueueTest() {
		//given
		Long concertId = 1L;
		long enterNum = 3;

		// Queue에 20명 진입
		for (int i = 0; i < 20; i++) {
			redisQueueManager.addQueue(concertId, i + 1L);
		}

		//when
		List<String> result = redisQueueManager.popUserFromWatingQueue(concertId, enterNum);

		//then
		assertNotNull(result);
		// pop한 유저의 수와 result.size()와 같아야 한다.
		assertEquals(enterNum, result.size());
	}

	@DisplayName("findWorkingUserNum 테스트")
	@Test
	void findWorkingUserNumTest() {
		//given
		Long concertId = 1L;
		String key = WORKING_KEY + concertId;

		for (int i = 0; i < 10; i++) {
			zSetOperations.add(key, String.valueOf(i + 1L), System.currentTimeMillis());
		}

		//when
		Long result = redisQueueManager.findWorkingUserNum(concertId);

		//then
		assertNotNull(result);
		assertEquals(10, result);
	}

	@DisplayName("addWorkingQueue 테스트")
	@Test
	void addWorkingQueueTest() {
		//given
		Long concertId = 1L;
		List<String> userList = new ArrayList<>();
		int listNum = 10;
		setUser(listNum);

		for (int i = 0; i < listNum; i++) {
			userList.add(String.valueOf(i + 1));
		}

		//when
		int result = redisQueueManager.addWorkingQueue(concertId, userList);

		//then
		assertNotNull(result);
		assertEquals(listNum, result);

		Long size = zSetOperations.size(WORKING_KEY + concertId);
		assertEquals(size, result);
	}

	void setUser(int num) {
		for (int i = 0; i < num; i++) {
			User user = User.builder()
				.name("testUser")
				.point(10L)
				.updatedAt(LocalDateTime.now())
				.build();
			userStore.save(user);
		}
	}

	@DisplayName("popFromWoringQueue 테스트")
	@Test
	void popFromWoringQueueTest() {
		//given
		Long concertId = 1L;
		String key = WORKING_KEY + concertId;

		for (int i = 0; i < 5; i++) {
			zSetOperations.add(key, String.valueOf(i + 1L), System.currentTimeMillis());
		}

		//when
		redisQueueManager.popFromWorkingQueue(concertId, 4L);

		//then
		Long size = zSetOperations.size(key);

		assertNotNull(size);
		assertEquals(5 - 1, size);
	}
}
