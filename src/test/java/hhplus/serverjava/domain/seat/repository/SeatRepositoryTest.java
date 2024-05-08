package hhplus.serverjava.domain.seat.repository;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.INVALID_SEAT;
import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FIND_CONCERT;
import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FOUND_CONCERT_OPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concert.infrastructure.ConcertJpaRepository;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.infrastructure.ConcertOptionJpaRepository;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.seat.infrastructure.SeatJpaRepository;

@SpringBootTest
@ActiveProfiles("prod")
public class SeatRepositoryTest {

	@Autowired
	private SeatJpaRepository seatJpaRepository;

	@Autowired
	private ConcertOptionJpaRepository concertOptionJpaRepository;

	@Autowired
	private ConcertJpaRepository concertJpaRepository;

	Concert concert;
	ConcertOption concertOption;
	LocalDateTime targetDate;

	@BeforeEach
	void setUp() {
		concert = concertJpaRepository.findById(1L)
			.orElseThrow(() -> new BaseException(NOT_FIND_CONCERT));

		concertOption = concertOptionJpaRepository.findById(1L)
			.orElseThrow(() -> new BaseException(NOT_FOUND_CONCERT_OPTION));

		targetDate = concertOption.getConcertAt();
	}

	@DisplayName("index 테스트")
	@Test
	void indexTest() {
		//given
		int seatNum = 48125;

		//when
		long startTime = System.currentTimeMillis();
		Optional<Seat> seat = seatJpaRepository.findAvailableSeat(
			concertOption.getId(),
			targetDate,
			Seat.State.AVAILABLE,
			seatNum
		);
		long endTime = System.currentTimeMillis();

		// 실행 시간 출력
		System.out.println("Execution time: " + (endTime - startTime) + "ms");
		// 451ms
		// 443ms
		// 442ms

		// 445

		// 329ms
		// 326ms
		// 337ms

		// 330

		// 약 25% 개선

		//then
		Seat findSeat = seat.orElseThrow(() -> new BaseException(INVALID_SEAT));
		assertEquals(seatNum, findSeat.getSeatNum());
	}

}
