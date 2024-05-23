package hhplus.serverjava.api.usecase.point;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import hhplus.serverjava.api.user.usecase.v2.UserPointChargeUseCaseV2;
import hhplus.serverjava.api.user.usecase.v2.UserUsePointUseCaseV2;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.infrastructure.UserJpaRepository;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PointIntegrationTestV2 {
	// @checkstyle:off
	@Autowired
	private UserPointChargeUseCaseV2 userPointChargeUseCaseV2;
	@Autowired
	private UserUsePointUseCaseV2 userUsePointUseCaseV2;
	@Autowired
	private UserStore userStore;
	@Autowired
	private UserReader userReader;
	@Autowired
	private UserJpaRepository userJpaRepository;

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
		User user = User.builder()
			.point(5000L)
			.name("testUser")
			.updatedAt(LocalDateTime.now())
			.build();
		userStore.save(user);
	}

	@AfterEach
	void after() {
		userJpaRepository.delete(userReader.findUser(1L));
	}

	/*
	 * 테스트 시나리오 ( 동시성 테스트 )
	 * 1명의 유저가 동시에 5번 충전 요청
	 * 모두 충전되어야 한다
	 * */
	@DisplayName("charge 동시성 테스트 (분산락)")
	@Test
	void chargeTest() throws InterruptedException {
		//given
		Long chargePoint = 5000L;

		AtomicInteger successCnt = new AtomicInteger(0);
		AtomicInteger failCnt = new AtomicInteger(0);

		int threadCnt = 5;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCnt);
		CountDownLatch latch = new CountDownLatch(threadCnt);

		//when
		for (int i = 0; i < threadCnt; i++) {
			try {
				executorService.execute(() -> {
					try {
						userPointChargeUseCaseV2.charge(1L, chargePoint);
						successCnt.incrementAndGet();
					} catch (Exception e) {
						System.out.println("Exception E = " + e.getMessage());
						failCnt.incrementAndGet();
					}
				});
			} finally {
				latch.countDown();
			}
		}
		latch.await();

		Thread.sleep(1000);

		//then
		User findUser = userReader.findUser(1L);
		System.out.println("findUser.getPoint() = " + findUser.getPoint());
		System.out.println("successCnt = " + successCnt);
		System.out.println("failCnt = " + failCnt);

		// 모두 성공
		assertEquals(threadCnt, successCnt.intValue());
		assertEquals(0, failCnt.intValue());

		// 충전 금액 검증 -> 초기 유저 포인트 + (충전 포인트 * 스레드 갯수)
		assertEquals(5000 + (chargePoint * threadCnt), findUser.getPoint());
	}

	/*
	 * 테스트 시나리오 ( 동시성 테스트 )
	 * 1명의 유저가 동시에 5번 포인트 사용 요청
	 * 모두 사용되어야 한다
	 * */
	@DisplayName("use 동시성 테스트")
	@Test
	void useTest() throws InterruptedException {
		//given
		Long usePoint = 500L;

		AtomicInteger successCnt = new AtomicInteger(0);
		AtomicInteger failCnt = new AtomicInteger(0);

		int threadCnt = 5;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCnt);
		CountDownLatch latch = new CountDownLatch(threadCnt);

		//when
		for (int i = 0; i < threadCnt; i++) {
			try {
				executorService.execute(() -> {
					try {
						userUsePointUseCaseV2.use(1L, usePoint);
						successCnt.incrementAndGet();
					} catch (Exception e) {
						System.out.println("Exception E = " + e.getMessage());
						failCnt.incrementAndGet();
					}
				});
			} finally {
				latch.countDown();
			}
		}
		latch.await();

		Thread.sleep(1000);

		//then
		User findUser = userReader.findUser(1L);

		// 모두 성공
		assertEquals(threadCnt, successCnt.intValue());
		assertEquals(threadCnt - successCnt.intValue(), failCnt.intValue());

		// 모두 사용 금액 검증 -> 초기 유저 포인트 - (사용 포인트 * 스레드 갯수)
		assertEquals(5000 - (usePoint * threadCnt), findUser.getPoint());
	}
}