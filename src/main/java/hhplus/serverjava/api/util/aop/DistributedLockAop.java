package hhplus.serverjava.api.util.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import hhplus.serverjava.api.util.aop.component.AopForTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @DistributedLock 선언 시 수행되는 Aop class
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {
	private static final String REDIS_LOCK_PREFIX = "LOCK:";

	private final RedissonClient redissonClient;
	private final AopForTransaction aopForTransaction;

	@Around("@annotation(hhplus.serverjava.api.util.aop.DistributedLock)")
	public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

		String key = REDIS_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
			signature.getParameterNames(),
			joinPoint.getArgs(),
			distributedLock.key().toString()
		);
		// 락의 이름으로 RLock 인스턴스를 가져온다.
		RLock rLock = redissonClient.getLock(key);
		log.info("rLock : {}", rLock.getName());

		try {
			// 정의된 waitTime까지 획득을 시도한다, 정의된 leaseTime이 지나면 잠금을 해제한다.
			boolean available = rLock.tryLock(
				distributedLock.waitTime(),
				distributedLock.leaseTime(),
				distributedLock.timeUnit()
			);
			if (!available) {
				log.info("분산락 획득 실패");
				return false;
			}
			log.info("분산락 획득 성공!");
			// DistributedLock 어노테이션이 선언된 메서드를 별도의 트랜잭션으로 실행한다.
			return aopForTransaction.precees(joinPoint);
		} catch (InterruptedException e) {
			throw new InterruptedException();
		} finally {
			try {
				// 종료 시 무조건 락을 해제한다.
				rLock.unlock();
			} catch (IllegalMonitorStateException e) {
				log.info("Redisson Lock Already UnLock {} {},", method.getName(), key);
			}
		}
	}
}
