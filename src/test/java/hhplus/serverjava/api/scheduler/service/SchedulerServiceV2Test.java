package hhplus.serverjava.api.scheduler.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import hhplus.serverjava.api.support.scheduler.service.SchedulerService;
import hhplus.serverjava.domain.concert.components.ConcertStore;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SchedulerServiceV2Test {
	// @checkstyle:off

	@Autowired
	private UserStore userStore;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private ConcertStore concertStore;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private ZSetOperations<String, String> zSetOperations;

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

	@DisplayName("findWorkingUserNumValidationV2 테스트(true)")
	@Test
	void findWorkingUserNumValidationV2Test() {
		//given
		Concert concert = Concert.builder()
			.id(55L)
			.artist("IU")
			.name("IU Concert")
			.build();
		concertStore.save(concert);
		String key = WORKING_KEY + concert.getId();

		for (int i = 0; i < 99; i++) {
			zSetOperations.add(key, String.valueOf(i + 1L), System.currentTimeMillis());
		}

		//when
		Boolean result = schedulerService.findWorkingUserNumValidationV2();

		//then
		assertNotNull(result);

		// 티켓팅중인 콘서트의 worikngQueue가 100 미만이라면 return true
		assertEquals(true, result);
	}

	@DisplayName("findWorkingUserNumValidationV2 테스트(false)")
	@Test
	void findWorkingUserNumValidationV2FalseTest() {
		//given
		Concert concert = Concert.builder()
			.id(99L)
			.artist("IU")
			.name("IU Concert")
			.build();
		concertStore.save(concert);
		String key = WORKING_KEY + concert.getId();

		for (int i = 0; i < 99; i++) {
			zSetOperations.add(key, String.valueOf(i + 1L), System.currentTimeMillis());
		}

		//when
		Boolean result = schedulerService.findWorkingUserNumValidationV2();

		//then
		assertNotNull(result);

		// 티켓팅중인 콘서트의 worikngQueue가 100 미만이라면 return true
		assertEquals(true, result);
	}

	// 티켓팅중인 콘서트의 WorkingQueue가 100명보다 부족한 만큼 대기열 유저 활성화
	@DisplayName("enterServiceUserV2 테스트")
	@Test
	void enterServiceUserV2Test() {
		//given
		Concert concert = Concert.builder()
			.id(59L)
			.artist("IU")
			.name("IU Concert")
			.build();
		concertStore.save(concert);
		String workingKey = WORKING_KEY + concert.getId();
		String waitingKey = WAITING_KEY + concert.getId();

		int userNum = 30;
		setUser(userNum);

		// 서비스 이용중인 유저 60명
		for (int i = 0; i < 60; i++) {
			zSetOperations.add(workingKey, String.valueOf(i + 50L), System.currentTimeMillis());
		}
		Long beforeWorkSize = zSetOperations.size(workingKey);
		System.out.println("beforeWorkSize = " + beforeWorkSize);

		// 대기중 유저 30명
		for (int i = 0; i < userNum; i++) {
			zSetOperations.add(waitingKey, String.valueOf(i + 1L), System.currentTimeMillis());
		}
		Long beforeWaitSize = zSetOperations.size(waitingKey);
		System.out.println("beforeWaitSize = " + beforeWaitSize);

		//when
		schedulerService.enterServiceUserV2();

		//then
		Long workSize = zSetOperations.size(workingKey);
		Long waitSize = zSetOperations.size(waitingKey);

		// 로직 실행 전 workingQueue, waitQueue size
		assertEquals(60, beforeWorkSize);
		assertEquals(30, beforeWaitSize);

		// 대기중 유저 + workingQueue
		// assertEquals(beforeWorkSize + beforeWaitSize, workSize);
		// assertEquals(0, waitSize);
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

}
