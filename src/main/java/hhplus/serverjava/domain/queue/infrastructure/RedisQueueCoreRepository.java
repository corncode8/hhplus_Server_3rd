package hhplus.serverjava.domain.queue.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.queue.repository.RedisQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisQueueCoreRepository implements RedisQueueRepository {

	private final RedisTemplate<String, String> redisTemplate;

	private ZSetOperations<String, String> zSetOperations;

	private final String WAITING_KEY = "waiting:concert:";
	private final String WORKING_KEY = "working:concert:";

	@PostConstruct
	public void init() {
		zSetOperations = redisTemplate.opsForZSet();
	}

	@Override
	public Long addWaitingQueue(Long concertId, Long userId) {

		String key = WAITING_KEY + concertId;
		zSetOperations.add(key, userId.toString(), System.currentTimeMillis());
		redisTemplate.expire(key, 5, TimeUnit.HOURS);

		Long rank = zSetOperations.rank(key, userId.toString());
		return rank;
	}

	@Override
	public Long findUserRank(Long concertId, Long userId) {
		Long rank = zSetOperations.rank(WAITING_KEY + concertId, userId.toString());
		return rank != null ? rank : -1L;
	}

	@Override
	public List<String> popWaitingQueue(Long concertId, long enterNum) {
		String key = WAITING_KEY + concertId;

		Set<ZSetOperations.TypedTuple<String>> poppedUsers = zSetOperations.popMin(key, enterNum);

		List<String> userIds = new ArrayList<>();

		for (ZSetOperations.TypedTuple<String> user : poppedUsers) {
			userIds.add(user.getValue());
		}

		return userIds;
	}

	@Override
	public Long addWorkingQueue(Long concertId, Long userId) {
		String key = WORKING_KEY + concertId;

		zSetOperations.add(key, userId.toString(), System.currentTimeMillis());
		redisTemplate.expire(key, 10, TimeUnit.MINUTES);

		return userId;
	}

	@Override
	public Long workingUserNum(Long concertId) {
		String key = WORKING_KEY + concertId;

		return zSetOperations.size(key);
	}

	@Override
	public void popWoringQueue(Long concertId, Long userId) {
		String key = WORKING_KEY + concertId;
		zSetOperations.remove(key, userId.toString());
	}
}
