package hhplus.serverjava.domain.queue.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface RedisQueueRepository {
	Long findUserRank(Long concertId, Long userId);

	Long addWaitingQueue(Long concertId, Long userId);

	List<String> popWaitingQueue(Long concertId, long enterNum);

	Long workingUserNum(Long concertId);

	Long addWorkingQueue(Long concertId, Long userId);

	void popWoringQueue(Long concertId, Long userId);
}
