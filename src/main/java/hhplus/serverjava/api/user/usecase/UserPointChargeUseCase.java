package hhplus.serverjava.api.user.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.user.response.UserPoint;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPointChargeUseCase {

	private final UserReader userReader;

	// 포인트 충전
	public UserPoint charge(Long userId, Long amount) {

		// 비관적 락 적용
		User user = userReader.findByIdWithLock(userId);
		user.sumPoint(amount);

		return new UserPoint(user.getId(), user.getPoint());
	}
}
