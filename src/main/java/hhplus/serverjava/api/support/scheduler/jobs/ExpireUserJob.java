package hhplus.serverjava.api.support.scheduler.jobs;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.support.scheduler.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.SCHEDULER_ERROR;

@Slf4j
public class ExpireUserJob implements Job {
    public void execute(JobExecutionContext context) {
        // 서비스에 입장한 후 10분이 지나 결제를 안 한 유저 만료 처리
        try {
            SchedulerContext schedulerContext = context.getScheduler().getContext();

            ApplicationContext applicationContext = (ApplicationContext) schedulerContext.get("applicationContext");

            SchedulerService schedulerService = applicationContext.getBean(SchedulerService.class);

            LocalDateTime now = LocalDateTime.now();

            // 유저 만료 처리
            schedulerService.expireUsers(now);


        } catch (SchedulerException e) {
            log.error("ExpireUserJob Error");
            log.error(e.getMessage());
            throw new BaseException(SCHEDULER_ERROR);
        }

    }
}
