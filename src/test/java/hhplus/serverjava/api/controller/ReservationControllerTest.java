package hhplus.serverjava.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.ObjectMapper;

import hhplus.serverjava.api.reservation.request.PostReservationRequest;
import hhplus.serverjava.api.util.jwt.JwtService;
import hhplus.serverjava.domain.concert.components.ConcertStore;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionStore;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.seat.components.SeatStore;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
// @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserStore userStore;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private SeatStore seatStore;
	@Autowired
	private ConcertOptionStore concertOptionStore;

	@Autowired
	private ConcertStore concertStore;

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

	@DisplayName("콘서트 조회 테스트")
	@Test
	void getConcertTest() throws Exception {
		//given
		User user = User.builder()
			.name("testUser")
			.point(5000000L)
			.updatedAt(LocalDateTime.now())
			.build();
		userStore.save(user);

		String jwt = jwtService.createJwt(user.getId());
		int concertListSize = 5;

		for (int i = 0; i < concertListSize; i++) {
			Concert concert = new Concert(i + 1L, "IU CONCERT" + i, "IU" + i);
			concertStore.save(concert);
		}

		mockMvc.perform(get("/api/concert")
				.header("Authorization", jwt)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
			.andExpect(jsonPath("$.result.concertInfoList.size()").value(concertListSize));
	}

	@DisplayName("예약 가능한 날짜 조회 테스트")
	@Test
	void getAvailableDatesTest() throws Exception {
		//given
		User user = User.builder()
			.name("testUser")
			.point(5000000L)
			.updatedAt(LocalDateTime.now())
			.build();
		userStore.save(user);

		String jwt = jwtService.createJwt(user.getId());

		Concert concert = new Concert(1L, "IU CONCERT", "IU");
		concertStore.save(concert);
		int availableDates = 3;

		for (int i = 0; i < availableDates; i++) {
			ConcertOption concertOption = ConcertOption.builder()
				.concertAt(LocalDateTime.now().plusDays(i + 3))
				.build();
			concertOption.addConcert(concert);
			concertOptionStore.save(concertOption);
		}

		mockMvc.perform(get("/api/concert/{concertId}/date", concert.getId())
				.header("Authorization", jwt)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.message").value("요청에 성공하였습니다."));
	}

	@DisplayName("예약 가능한 좌석 조회 테스트")
	@Test
	void getAvailableSeatsTest() throws Exception {
		//given
		User user = User.builder()
			.name("testUser")
			.point(5000000L)
			.updatedAt(LocalDateTime.now())
			.build();
		userStore.save(user);

		String jwt = jwtService.createJwt(user.getId());

		Concert concert = new Concert(1L, "IU CONCERT", "IU");
		concertStore.save(concert);

		LocalDateTime testDateTime = LocalDateTime.now().plusDays(3).truncatedTo(ChronoUnit.HOURS);

		ConcertOption concertOption = ConcertOption.builder()
			.concertAt(testDateTime)
			.build();
		concertOption.addConcert(concert);
		concertOptionStore.save(concertOption);

		int seatListSize = 20;
		for (int i = 0; i < seatListSize; i++) {
			Seat seat = Seat.builder()
				.seatNum(i + 1)
				.price(50000)
				.concert(concert)
				.concertOption(concertOption)
				.build();
			seatStore.save(seat);
		}

		mockMvc.perform(
				get("/api/concert/date/{date}/seats", testDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
					.header("Authorization", jwt)
					.param("concertId", concert.getId().toString())
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
			.andExpect(jsonPath("$.result.concertOptionId").value(concertOption.getId()))
			.andExpect(jsonPath("$.result.seatList.size()").value(seatListSize));
	}

	@DisplayName("콘서트 예약 테스트")
	@Test
	void bookingConcertTest() throws Exception {
		//given
		User user = User.builder()
			.name("testUser")
			.point(5000000L)
			.updatedAt(LocalDateTime.now())
			.build();
		userStore.save(user);

		String jwt = jwtService.createJwt(user.getId());

		Concert concert = new Concert(1L, "IU CONCERT", "IU");
		concertStore.save(concert);

		LocalDateTime testDateTime = LocalDateTime.now().plusDays(3).truncatedTo(ChronoUnit.HOURS);

		ConcertOption concertOption = ConcertOption.builder()
			.concertAt(testDateTime)
			.build();
		concertOption.addConcert(concert);
		concertOptionStore.save(concertOption);

		Seat seat = Seat.builder()
			.seatNum(10)
			.price(50000)
			.concert(concert)
			.concertOption(concertOption)
			.build();
		seatStore.save(seat);

		PostReservationRequest request =
			new PostReservationRequest(concertOption.getId(), testDateTime.toString(), seat.getSeatNum());

		String expectedFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(testDateTime);

		mockMvc.perform(post("/api/reservation")
				.header("Authorization", jwt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))

			// 콘서트 검증
			.andExpect(jsonPath("$.result.concertName").value(concert.getName()))
			.andExpect(jsonPath("$.result.concertArtist").value(concert.getArtist()))

			// 예약 시간, 좌석 번호, 좌석 가격
			.andExpect(jsonPath("$.result.reservationDate").value(expectedFormat))
			.andExpect(jsonPath("$.result.reservationSeat").value(seat.getSeatNum()))
			.andExpect(jsonPath("$.result.price").value(seat.getPrice()));
	}

}
