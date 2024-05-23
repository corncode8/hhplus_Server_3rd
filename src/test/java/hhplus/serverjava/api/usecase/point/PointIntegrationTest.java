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

import hhplus.serverjava.api.user.usecase.UserPointChargeUseCase;
import hhplus.serverjava.api.user.usecase.UserUsePointUseCase;
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
public class PointIntegrationTest {
	// @checkstyle:off
	@Autowired
	private UserPointChargeUseCase userPointChargeUseCase;
	@Autowired
	private UserUsePointUseCase userUsePointUseCase;
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
		.withExposedPorts(6379)
		.withReuse(true);

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
	 * */
	@DisplayName("charge 동시성 테스트")
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
						userPointChargeUseCase.charge(1L, chargePoint);
						successCnt.incrementAndGet();
					} catch (Exception e) {
						System.out.println("ExceptionEE = " + e.getMessage());
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

		// 전체 스레드 갯수 - 실패한 횟수 = 성공한 횟수
		assertEquals(threadCnt - failCnt.intValue(), successCnt.intValue());

		// version == 성공한 횟수
		assertEquals(successCnt.intValue(), findUser.getVersion());

		// 한 건만 충전 성공
		assertEquals(5000 + 5000, findUser.getPoint());
	}

	/*
	 * 테스트 시나리오 ( 동시성 테스트 )
	 * 1명의 유저가 동시에 5번 포인트 사용 요청
	 * */
	@DisplayName("use 동시성 테스트")
	@Test
	void useTest() throws InterruptedException {
		//given
		Long usePoint = 5000L;

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
						userUsePointUseCase.use(1L, usePoint);
						successCnt.incrementAndGet();
					} catch (Exception e) {
						System.out.println("ExceptionEE = " + e.getMessage());
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

		// 전체 스레드 갯수 - 실패한 횟수 = 성공한 횟수
		assertEquals(threadCnt - failCnt.intValue(), successCnt.intValue());

		// version == 성공한 횟수
		assertEquals(successCnt.intValue(), findUser.getVersion());

		// 한 건만 사용 성공
		assertEquals(5000 - 5000, findUser.getPoint());
	}

	/*
	 * 테스트 시나리오 ( 동시성 테스트 )
	 * 1명의 유저가 동시에 5번 포인트 충전 및 차감 요청
	 * 가장 먼저 요청된 작업만 성공
	 * */
	@DisplayName("충전 및 차감 동시성 테스트")
	@Test
	void test() throws InterruptedException {
		//given
		Long point = 4000L;

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
						userUsePointUseCase.use(1L, point);
						userPointChargeUseCase.charge(1L, point);
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

		// 전체 스레드 갯수 - 실패한 횟수 = 성공한 횟수
		assertEquals(threadCnt - failCnt.intValue(), successCnt.intValue());

		// 사용, 차감 한번씩 실행
		assertEquals(2L, findUser.getVersion());
	}
}