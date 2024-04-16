package hhplus.serverjava.api.support.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class JobListener implements org.quartz.JobListener {

    // JobListener의 이름
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Job이 수행되기 전 상태
     *   - TriggerListener.vetoJobExecution == false
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        System.out.println(String.format("[%-18s][%s] 작업시작", "jobToBeExecuted", context.getJobDetail().getKey().toString()));
    }

    /**
     * Job이 중단된 상태
     *   - TriggerListener.vetoJobExecution == true
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        System.out.println(String.format("[%-18s][%s] 작업중단", "jobExecutionVetoed", context.getJobDetail().getKey().toString()));
    }

    /**
     * Job 수행이 완료된 상태
     */
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        System.out.println(String.format("[%-18s][%s] 작업완료", "jobWasExecuted", context.getJobDetail().getKey().toString()));
    }
}
