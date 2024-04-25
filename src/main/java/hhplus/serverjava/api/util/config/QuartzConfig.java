package hhplus.serverjava.api.util.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

	private final ApplicationContext applicationContext;

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		log.info("Config_applicationContext : {}", applicationContext);
		SchedulerFactoryBean factory = new SchedulerFactoryBean();

		factory.setApplicationContextSchedulerContextKey("applicationContext");
		factory.setApplicationContext(applicationContext);

		return factory;
	}
}
