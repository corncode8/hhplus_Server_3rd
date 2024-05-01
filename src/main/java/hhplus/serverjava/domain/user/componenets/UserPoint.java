package hhplus.serverjava.domain.user.componenets;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FIND_USER;

import org.springframework.stereotype.Component;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserReaderRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserPoint {

	private final UserReaderRepository userReaderRepository;

	public Long getPoint(Long id) {
		User user = userReaderRepository.findById(id)
			.orElseThrow(() -> new BaseException(NOT_FIND_USER));

		return user.getPoint();
	}

	public User pointCharge(Long id, Long point) {
		User user = userReaderRepository.findById(id)
			.orElseThrow(() -> new BaseException(NOT_FIND_USER));
		user.sumPoint(point);

		return user;
	}

	public User pointUse(Long id, Long point) {
		User user = userReaderRepository.findById(id)
			.orElseThrow(() -> new BaseException(NOT_FIND_USER));
		user.usePoint(point);

		return user;
	}
}
