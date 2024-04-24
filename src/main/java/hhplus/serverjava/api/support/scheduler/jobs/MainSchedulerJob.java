package hhplus.serverjava.api.support.scheduler.jobs;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.*;

import java.time.LocalDateTime;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.ApplicationContext;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.support.scheduler.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainSchedulerJob implements Job {

	// 좌석이 만료된 예약을 조회 ( 좌석 5분 임시배정 )
	// 서비스에 입장한 후 10분이 지나도록 결제를 안하고 있는 유저 조회
	// 서비스를 이용중인 유저가 100명 미만인지 조회

	public void execute(JobExecutionContext context) throws JobExecutionException {
		Scheduler scheduler = context.getScheduler();

		try {
			SchedulerContext schedulerContext = context.getScheduler().getContext();

			ApplicationContext applicationContext = (ApplicationContext)schedulerContext.get("applicationContext");

			SchedulerService schedulerService = applicationContext.getBean(SchedulerService.class);

			LocalDateTime now = LocalDateTime.now();

			// 좌석이 만료된 예약 조회
			// 좌석이 만료된 예약이 있다면 좌석을 만료시키는 스케줄러 동작
			if (schedulerService.findExpiredReservations(now)) {
				scheduleJob(scheduler, ActivateExpiredSeatsJob.class, "ActivateExpiredSeatsJob", "jobGroup1");
			}

			// 서비스에 입장한 후 10분이 지나도록 결제를 안하고 있는 유저가 있다면
			// 유저를 만료시키는 스케줄러 동작
			if (schedulerService.findUserTimeValidation(now)) {
				scheduleJob(scheduler, ExpireUserJob.class, "ExpireUserJob", "jobGroup2");
			}

			// 서비스를 이용중인 유저가 100명 미만이라면
			// 유저를 서비스에 입장시키는 스케줄러 동작
			if (schedulerService.findWorkingUserNumValidation(now)) {
				scheduleJob(scheduler, EnterServiceUsersJob.class, "EnterServiceUsersJob", "jobGroup3");
			}

		} catch (SchedulerException e) {
			log.error("MainSchedulerJob Error");
			log.error(e.getMessage());
			throw new BaseException(SCHEDULER_ERROR);
		}

	}

	private void scheduleJob(Scheduler scheduler, Class<? extends Job> jobClass, String jobName,
		String groupName) throws JobExecutionException {
		try {
			JobDetail jobDetail = JobBuilder.newJob(jobClass)
				.withIdentity(jobName, groupName).build();

			Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("trigger" + jobName, groupName)
				.startNow()
				.build();

			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new JobExecutionException(e);
		}
	}
}

