package hhplus.serverjava.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

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

import hhplus.serverjava.api.payment.request.PostPayRequest;
import hhplus.serverjava.api.util.jwt.JwtService;
import hhplus.serverjava.domain.reservation.components.ReservationStore;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.components.SeatStore;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
// @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PaymentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserStore userStore;

	@Autowired
	private SeatStore seatStore;

	@Autowired
	private ReservationStore reservationStore;

	@Autowired
	private JwtService jwtService;
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

	@DisplayName("결제 테스트 (결제 성공)")
	@Test
	void paymentTest() throws Exception {
		//given
		User user = User.builder()
			.name("testUser")
			.point(5000000L)
			.updatedAt(LocalDateTime.now())
			.build();
		userStore.save(user);

		String jwt = jwtService.createJwt(user.getId());

		Seat seat = Seat.builder()
			.seatNum(10)
			.price(5000)
			.build();
		seat.setExpiredAt(LocalDateTime.now().minusMinutes(1));
		seatStore.save(seat);

		Reservation reservation = Reservation.builder()
			.seatNum(10)
			.reservedPrice(5000)
			.user(user)
			.seat(seat)
			.concertAt(LocalDateTime.now().plusDays(5))
			.concertName("IU")
			.concertArtist("IUUUUUU")
			.build();
		reservationStore.save(reservation);

		PostPayRequest request = new PostPayRequest(reservation.getId(), reservation.getReservedPrice(), 1L);

		mockMvc.perform(post("/api/payment")
				.header("Authorization", jwt)   // 토큰 추가
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true)) // isSuccess
			.andExpect(jsonPath("$.code").value(200))       // code
			.andExpect(jsonPath("$.message").value("요청에 성공하였습니다.")) // successMessage
			.andExpect(jsonPath("$.result.reservationId").value(reservation.getId()))   // 예약 ID 검증
			.andExpect(jsonPath("$.result.payAmount").value(reservation.getReservedPrice()));   // 예약 금액 검증
	}

	@DisplayName("결제 테스트 (결제 실패 - 잔액 부족)")
	@Test
	void paymentFailTest() throws Exception {
		//given
		User user = User.builder()
			.name("testUser")
			.point(50L)
			.updatedAt(LocalDateTime.now())
			.build();
		userStore.save(user);

		String jwt = jwtService.createJwt(user.getId());

		Seat seat = Seat.builder()
			.seatNum(10)
			.price(5000)
			.build();
		seat.setExpiredAt(LocalDateTime.now().minusMinutes(1));
		seatStore.save(seat);

		Reservation reservation = Reservation.builder()
			.seatNum(10)
			.reservedPrice(5000)
			.user(user)
			.seat(seat)
			.concertAt(LocalDateTime.now().plusDays(5))
			.concertName("IU")
			.concertArtist("IUUUUUU")
			.build();
		reservationStore.save(reservation);

		PostPayRequest request = new PostPayRequest(reservation.getId(), reservation.getReservedPrice(), 1L);

		mockMvc.perform(post("/api/payment")
				.header("Authorization", jwt)   // 토큰 추가
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(false))        // isSuccess
			.andExpect(jsonPath("$.code").value(400))               // code
			.andExpect(jsonPath("$.message").value("포인트가 부족합니다.")); // failMessage

	}
}
