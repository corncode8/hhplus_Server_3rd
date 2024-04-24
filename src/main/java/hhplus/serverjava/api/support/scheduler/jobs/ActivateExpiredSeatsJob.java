package hhplus.serverjava.api.support.scheduler.jobs;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.*;

import java.time.LocalDateTime;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.support.scheduler.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActivateExpiredSeatsJob implements Job {
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			SchedulerContext schedulerContext = context.getScheduler().getContext();

			ApplicationContext applicationContext = (ApplicationContext)schedulerContext.get("applicationContext");

			SchedulerService schedulerService = applicationContext.getBean(SchedulerService.class);

			LocalDateTime now = LocalDateTime.now();
			schedulerService.expireReservations(now);

		} catch (SchedulerException e) {
			log.error("ActivateExpiredSeatsJob Error");
			log.error(e.getMessage());
			throw new BaseException(SCHEDULER_ERROR);
		}
	}
}
