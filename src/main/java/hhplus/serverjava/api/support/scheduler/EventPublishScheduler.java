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

import hhplus.serverjava.api.support.scheduler.jobs.EventPublishJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublishScheduler {

	private static Scheduler scheduler;

	@PostConstruct
	public void quartzSchedulerMethod() throws SchedulerException {

		String jobName = "eventPublishJob";
		String triggerName = "eventPublishTrigger";

		String uniqueJobName = jobName + "_" + System.currentTimeMillis();
		String uniqueTriggerName = triggerName + "_" + System.currentTimeMillis();

		scheduler = StdSchedulerFactory.getDefaultScheduler();

		JobDetail jobDetail = JobBuilder.newJob(EventPublishJob.class)
			.withIdentity(uniqueJobName, "group2").build();

		Trigger trigger = TriggerBuilder.newTrigger()
			.withIdentity(uniqueTriggerName, "group2")
			.startNow()
			.withSchedule(SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInMinutes(5)
				.repeatForever())
			.build();
		JobKey jobKey = new JobKey(uniqueJobName, "group2");

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
