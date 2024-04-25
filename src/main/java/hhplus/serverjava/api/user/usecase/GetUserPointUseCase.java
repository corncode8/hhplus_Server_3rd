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
public class GetUserPointUseCase {

	private final UserReader userReader;

	public UserPoint execute(Long userId) {
		User user = userReader.findUser(userId);
		return new UserPoint(user.getId(), user.getPoint());
	}

}
