package hhplus.serverjava.api.scheduler;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import hhplus.serverjava.api.support.scheduler.jobs.MainSchedulerJob;
import hhplus.serverjava.domain.reservation.components.ReservationReader;
import hhplus.serverjava.domain.reservation.components.ReservationStore;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.components.SeatReader;
import hhplus.serverjava.domain.seat.components.SeatStore;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;

@SpringBootTest
@ActiveProfiles("dev")
public class MainSchedulerJobTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private SeatStore seatStore;

	@Autowired
	private ReservationStore reservationStore;

	@Autowired
	private UserStore userStore;

	@Autowired
	private SeatReader seatReader;

	@Autowired
	private UserReader userReader;

	@Autowired
	private ReservationReader reservationReader;

	// Quartz Scheduler
	private Scheduler scheduler;

	@BeforeEach
	void setUp() throws SchedulerException {
		// 스케줄러 초기화
		scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.getContext().put("applicationContext", applicationContext);
		scheduler.start();
	}

	@AfterEach
	void schedulerDown() throws SchedulerException {
		if (scheduler != null) {
			scheduler.shutdown();
		}
	}

	@DisplayName("Quartz MainSchedulerJob 테스트")
	@Test
	void MainSchedulerJobTest() throws SchedulerException, InterruptedException {
		//given

		// 좌석이 만료된 예약 조회
		// 좌석이 만료된 예약이 있다면 좌석을 만료시키는 스케줄러 동작
		Seat seat = Seat.builder()
			.seatNum(50)
			.price(5000)
			.build();
		seat.setReserved();
		seat.setExpiredAt(LocalDateTime.now().minusMinutes(10));
		seatStore.save(seat);

		Reservation reservation = Reservation.builder()
			.concertAt(LocalDateTime.now().plusDays(5))
			.reservedPrice(seat.getPrice())
			.seat(seat)
			.concertName("MAKTUB CONCERT")
			.concertArtist("MAKTUB")
			.seatNum(seat.getSeatNum())
			.build();
		reservationStore.save(reservation);

		// 서비스에 입장한 후 10분이 지나도록 결제를 안하고 있는 유저가 있다면
		// 유저를 만료시키는 스케줄러 동작
		User user = User.builder()
			.name("testUsername")
			.point(500000L)
			.updatedAt(LocalDateTime.now())
			.build();
		user.setProcessing();
		user.setUpdatedAt(LocalDateTime.now().minusMinutes(15));
		userStore.save(user);

		//when
		JobDetail jobDetail = JobBuilder.newJob(MainSchedulerJob.class)
			.withIdentity("testJob", "testGroup1").build();
		Trigger trigger = TriggerBuilder.newTrigger()
			.withIdentity("testTrigger", "testGroup1")
			.startNow()
			.build();

		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.triggerJob(jobDetail.getKey());

		//then
		// 스케줄러가 일할 시간 부여
		Thread.sleep(10000);

		// seat이 만료되었다면 다시 AVAILABLE로 변했는지 검증
		Seat updSeat = seatReader.findSeatById(seat.getId());
		assertEquals(Seat.State.AVAILABLE, updSeat.getStatus());

		// 해당 예약이 취소처리가 되었는지 검증
		Reservation updReservaton = reservationReader.findReservation(reservation.getId());
		assertEquals(Reservation.State.CANCELLED, updReservaton.getStatus());

		// 10분동안 결제를 안한 유저라면 DONE으로 상태 변경됐는지 검증
		User updUser = userReader.findUser(user.getId());
		assertEquals(User.State.DONE, updUser.getStatus());
	}
}
