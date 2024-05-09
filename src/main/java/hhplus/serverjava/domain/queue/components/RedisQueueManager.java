package hhplus.serverjava.domain.queue.components;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FIND_USER;
import static hhplus.serverjava.api.support.response.BaseResponseStatus.WAIT_QUEUE_EMPTY;

import java.util.List;

import org.springframework.stereotype.Component;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.queue.infrastructure.RedisQueueCoreRepository;
import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisQueueManager {
	// @checkstyle:off

	private final UserStoreRepository userStoreRepository;
	private final RedisQueueCoreRepository redisQueueCoreRepository;

	/**
	 * 대기열에서 사용자의 순번을 조회
	 * @param concertId concert ID
	 * @param userId user ID
	 * @return 대기열에서 사용자의 순위 (0부터 시작, 없으면 -1 반환)
	 */
	public Long findUserRank(Long concertId, Long userId) {
		return redisQueueCoreRepository.findUserRank(concertId, userId);
	}

	/**
	 * 사용자를 대기열에 추가하고 TTL을 설정
	 * @param concertId concert ID
	 * @param userId user ID
	 * @return user의 rank
	 */
	public Long addQueue(Long concertId, Long userId) {
		return redisQueueCoreRepository.addWaitingQueue(concertId, userId);
	}

	/**
	 * 대기열에서 가장 우선순위가 높은 사용자를 제거
	 * @param concertId concert ID
	 * @param enterNum  서비스에 입장할 유저 수
	 * @return 서비스에 입장한 유저의 수
	 */
	public List<String> popUserFromWatingQueue(Long concertId, long enterNum) {
		List<String> popUserList = redisQueueCoreRepository.popWaitingQueue(concertId, enterNum);
		if (!popUserList.isEmpty()) {
			return popUserList;
		} else {
			throw new BaseException(WAIT_QUEUE_EMPTY);
		}
	}

	/**
	 * WorkingQueue에 있는 유저의 수를 조회
	 * @param concertId concert ID
	 * @return WorkingQueue에 있는 유저의 수
	 */
	public Long findWorkingUserNum(Long concertId) {
		return redisQueueCoreRepository.workingUserNum(concertId);
	}

	/**
	 * 사용자의 상태를 WORKING으로 변경 후 WorkingQueue에 추가
	 * @param concertId concert ID
	 * @param popUserList 서비스에 입장할 유저 List
	 * @return 서비스에 입장한 유저의 수
	 */
	public int addWorkingQueue(Long concertId, List<String> popUserList) {
		for (String userId : popUserList) {
			setUserStatus(userId);
			redisQueueCoreRepository.addWorkingQueue(concertId, Long.parseLong(userId));
		}
		return popUserList.size();
	}

	/**
	 * 사용자의 상태를 변경 (WAITING -> PROCESSING)
	 * @param userId user ID
	 */
	public void setUserStatus(String userId) {
		User user = userStoreRepository.findUser(Long.parseLong(userId))
			.orElseThrow(() -> new BaseException(NOT_FIND_USER));
		user.setProcessing();
	}

	/**
	 * WorkingQueue에 있는 유저 pop
	 * @param concertId concert ID
	 * @param userId user ID
	 */
	public void popFromWorkingQueue(Long concertId, Long userId) {
		redisQueueCoreRepository.popWoringQueue(concertId, userId);
	}
}
