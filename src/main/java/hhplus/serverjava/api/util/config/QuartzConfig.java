package hhplus.serverjava.api.util.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

	private final ApplicationContext applicationContext;

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();

		factory.setApplicationContext(applicationContext);
		factory.setApplicationContextSchedulerContextKey("applicationContext");

		return factory;
	}
}
