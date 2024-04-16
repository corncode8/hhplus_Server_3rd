package hhplus.serverjava.api.support.scheduler.jobs;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.api.support.scheduler.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.SCHEDULER_ERROR;

@Slf4j
public class ActivateExpiredSeatsJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            SchedulerContext schedulerContext = context.getScheduler().getContext();

            ApplicationContext applicationContext = (ApplicationContext) schedulerContext.get("applicationContext");

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
