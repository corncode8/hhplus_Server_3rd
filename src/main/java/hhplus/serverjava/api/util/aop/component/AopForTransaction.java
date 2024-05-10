package hhplus.serverjava.api.util.aop.component;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * AOP에서 트랜잭션 분리를 위한 클래스
 */
@Slf4j
@Component
public class AopForTransaction {

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Object precees(final ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("Redis Transaction 시작!");
		return joinPoint.proceed();
	}
}
