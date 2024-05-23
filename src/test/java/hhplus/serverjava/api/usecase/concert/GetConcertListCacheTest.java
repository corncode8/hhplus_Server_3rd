package hhplus.serverjava.api.usecase.concert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import hhplus.serverjava.api.concert.response.GetConcertResponse;
import hhplus.serverjava.api.concert.usecase.GetConcertListUseCase;
import hhplus.serverjava.domain.concert.components.ConcertStore;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concert.infrastructure.ConcertJpaRepository;

@SpringBootTest
@EnableCaching
@Testcontainers
@ActiveProfiles("test")
public class GetConcertListCacheTest {

	@Autowired
	private GetConcertListUseCase getConcertListUseCase;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private ConcertStore concertStore;

	@Autowired
	private ConcertJpaRepository concertJpaRepository;

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
		Concert concert = Concert.builder()
			.id(1L)
			.name("Cache")
			.artist("Chchetest")
			.build();
		concertStore.save(concert);
	}

	@DisplayName("테스트")
	@Test
	void test() {
		// 첫 번째 실행 - 데이터베이스에서 가져오고 캐시에 저장
		GetConcertResponse result = getConcertListUseCase.execute();

		assertNotNull(result);
		assertEquals(1, result.getConcertInfoList().size());

		// 캐시에서 데이터 확인
		SimpleValueWrapper cacheValue = (SimpleValueWrapper)cacheManager.getCache("concertList")
			.get(Concert.State.SHOWING);
		assertNotNull(cacheValue);
		assertNotNull(cacheValue.get());

		// 데이터베이스에서 데이터 삭제
		concertJpaRepository.deleteAll();

		// 두 번째 실행 - 캐시에서 데이터 가져옴 (쿼리 없이)
		GetConcertResponse onlyCache = getConcertListUseCase.execute();
		assertNotNull(onlyCache);
		assertEquals(1, onlyCache.getConcertInfoList().size());

		// 캐시에서 데이터 확인
		cacheValue = (SimpleValueWrapper)cacheManager.getCache("concertList").get(Concert.State.SHOWING);
		assertNotNull(cacheValue);
		assertNotNull(cacheValue.get());
	}
}
