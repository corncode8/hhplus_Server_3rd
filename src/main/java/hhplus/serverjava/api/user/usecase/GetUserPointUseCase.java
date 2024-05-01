package hhplus.serverjava.api.user.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.user.response.UserPointResponse;
import hhplus.serverjava.domain.user.componenets.UserPoint;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GetUserPointUseCase {
	private final UserPoint userPoint;

	public UserPointResponse execute(Long userId) {
		Long point = userPoint.getPoint(userId);
		return new UserPointResponse(userId, point);
	}

}
