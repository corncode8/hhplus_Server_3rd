package hhplus.serverjava.api.support.scheduler;

import javax.annotation.PostConstruct;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

import hhplus.serverjava.api.support.scheduler.jobs.MainSchedulerJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuartzScheduler {

	private static Scheduler scheduler;

	@PostConstruct
	public void quartzSchedulerMethod() throws SchedulerException {

		String jobName = "mainSchedulerJob";
		String triggerName = "mainTrigger";

		String uniqueJobName = jobName + "_" + System.currentTimeMillis();
		String uniqueTriggerName = triggerName + "_" + System.currentTimeMillis();

		// 스케줄러 팩토리를 통해 스케줄러 인스턴스 생성
		scheduler = StdSchedulerFactory.getDefaultScheduler();

		// 메인 스케줄러 Job과 트리거 설정
		// 메인 스케줄러는 1분마다 실행
		JobDetail jobDetail = JobBuilder.newJob(MainSchedulerJob.class)
			.withIdentity(uniqueJobName, "group1").build();

		Trigger trigger = TriggerBuilder.newTrigger()
			.withIdentity(uniqueTriggerName, "group1")
			.startNow()
			.withSchedule(SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInMinutes(1)
				.repeatForever())
			.build();
		JobKey jobKey = new JobKey(uniqueJobName, "group1");
		if (scheduler.checkExists(jobKey)) {
			scheduler.deleteJob(jobKey); // 이미 존재하면 삭제
		} else {
			scheduler.scheduleJob(jobDetail, trigger);
		}
	}

	public void shutdown() throws SchedulerException {
		if (scheduler != null) {
			scheduler.shutdown();
		}
	}
}
