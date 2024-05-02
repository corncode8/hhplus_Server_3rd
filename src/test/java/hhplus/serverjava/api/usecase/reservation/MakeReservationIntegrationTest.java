package hhplus.serverjava.api.usecase.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import hhplus.serverjava.api.reservation.request.PostReservationRequest;
import hhplus.serverjava.api.reservation.usecase.MakeReservationUseCase;
import hhplus.serverjava.domain.concert.components.ConcertStore;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionStore;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.seat.components.SeatReader;
import hhplus.serverjava.domain.seat.components.SeatStore;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
// @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MakeReservationIntegrationTest {

	@Autowired
	private UserStore userStore;
	@Autowired
	private SeatStore seatStore;
	@Autowired
	private SeatReader seatReader;
	@Autowired
	private ConcertStore concertStore;
	@Autowired
	private ConcertOptionStore concertOptionStore;
	@Autowired
	private MakeReservationUseCase makeReservationUseCase;

	@Container
	private static GenericContainer mySqlContainer = new MySQLContainer("mysql:8.0")
		.withReuse(true);

	/*
	 * 테스트 시나리오 ( 동시성 테스트 )
	 * 다수의 유저가 동시에 하나의 좌석을 선점하려는 경우 한명의 유저만 성공
	 * 5명이 동시에 좌석 예약 -> 한명만 성공 나머지는 실패
	 */
	@DisplayName("예약 낙관적 락 테스트")
	@Test
	void test() throws Exception {
		//given
		int testSeatNum = 10;
		LocalDateTime testDateTime = LocalDateTime.of(2024, 4, 11, 15, 30);

		List<User> user = Arrays.asList(
			saveTestUser(), saveTestUser(),
			saveTestUser(), saveTestUser(),
			saveTestUser()
		);

		Concert concert = saveTestConcert();

		ConcertOption concertOption = saveTestConcertOption(testDateTime, concert);

		Seat seat = saveTestSeat(testSeatNum, concertOption, concert);

		PostReservationRequest request = new PostReservationRequest(concertOption.getId(), testDateTime.toString(),
			seat.getSeatNum());

		AtomicInteger successCnt = new AtomicInteger(0);
		AtomicInteger failCnt = new AtomicInteger(0);

		int threadCnt = user.size();
		ExecutorService executorService = Executors.newFixedThreadPool(threadCnt);
		CountDownLatch latch = new CountDownLatch(threadCnt);

		//when
		for (int i = 0; i < threadCnt; i++) {
			final int idx = i;
			try {
				executorService.execute(() -> {
					try {
						makeReservationUseCase.makeReservation(user.get(idx).getId(), request);
						successCnt.incrementAndGet();
					} catch (Exception e) {
						System.out.println("Exception = " + e.getMessage());
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
		Seat findSeat = seatReader.findSeatById(seat.getId());

		// 전체 스레드 갯수 - 실패한 횟수 = 성공한 횟수
		assertEquals(threadCnt - failCnt.intValue(), successCnt.intValue());

		// version은 성공한 횟수만큼 증가
		assertEquals(1, findSeat.getVersion());

		// 전체 스레드 갯수 - 실패한 횟수
		assertEquals(threadCnt - successCnt.intValue(), failCnt.intValue());

	}

	private User saveTestUser() {
		User user = User.builder()
			.point(0L)
			.name("testUser")
			.updatedAt(LocalDateTime.now())
			.build();
		userStore.save(user);

		return user;
	}

	private Concert saveTestConcert() {
		Concert concert = Concert.builder()
			.artist("MAKTUB")
			.name("MAKTUB CONCERT")
			.build();
		concertStore.save(concert);

		return concert;
	}

	private ConcertOption saveTestConcertOption(LocalDateTime testDateTime, Concert concert) {
		ConcertOption concertOption = ConcertOption.builder()
			.concertAt(testDateTime)
			.build();
		concertOption.addConcert(concert);
		concertOptionStore.save(concertOption);

		return concertOption;
	}

	private Seat saveTestSeat(int testSeatNum, ConcertOption concertOption, Concert concert) {
		Seat seat = Seat.builder()
			.seatNum(testSeatNum)
			.price(50000)
			.concertOption(concertOption)
			.concert(concert)
			.build();
		seat.setAvailable();
		seat.getConcertOption().addSeatList(seat);
		seat.getConcert().addSeatList(seat);

		seatStore.save(seat);

		return seat;
	}
}
