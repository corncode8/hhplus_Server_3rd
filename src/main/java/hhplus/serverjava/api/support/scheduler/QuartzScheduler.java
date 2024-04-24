package hhplus.serverjava.api.support.scheduler;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

import hhplus.serverjava.api.support.scheduler.jobs.MainSchedulerJob;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuartzScheduler {

	private final Scheduler scheduler;

	public QuartzScheduler() throws SchedulerException {

		// 스케줄러 팩토리를 통해 스케줄러 인스턴스 생성
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();

		// 메인 스케줄러 Job과 트리거 설정
		// 메인 스케줄러는 1분마다 실행
		JobDetail jobDetail = JobBuilder.newJob(MainSchedulerJob.class)
			.withIdentity("mainSchedulerJob", "group1").build();

		Trigger trigger = TriggerBuilder.newTrigger()
			.withIdentity("mainTrigger", "group1")
			.startNow()
			.withSchedule(SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInMinutes(1)
				.repeatForever())
			.build();
		scheduler.scheduleJob(jobDetail, trigger);
	}

	public void shutdown() throws SchedulerException {
		if (scheduler != null) {
			scheduler.shutdown();
		}
	}
}
