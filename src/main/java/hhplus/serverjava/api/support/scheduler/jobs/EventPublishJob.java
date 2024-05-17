package hhplus.serverjava.api.support.scheduler.jobs;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.SCHEDULER_ERROR;

import javax.annotation.PostConstruct;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.support.scheduler.service.EventPublishService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventPublishJob implements Job {

	@PostConstruct
	public void execute(JobExecutionContext context) {
		Scheduler scheduler = context.getScheduler();

		try {
			SchedulerContext schedulerContext = context.getScheduler().getContext();

			ApplicationContext applicationContext = (ApplicationContext)schedulerContext.get("applicationContext");

			EventPublishService eventPublishService = applicationContext.getBean(EventPublishService.class);
			scheduler.start();

			// 이벤트 발행 ( 실패한 이벤트 중 발행된지 5분 이상 된 이벤트 )
			eventPublishService.publishDataSendEvent();

		} catch (SchedulerException e) {
			log.error("EventPublishJob Error");
			log.error(e.getMessage());
			throw new BaseException(SCHEDULER_ERROR);
		}
	}
}
