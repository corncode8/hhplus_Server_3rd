package hhplus.serverjava.domain.user.componenets;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_ENOUGH_POINT;
import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_VALID_USER;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.user.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserValidator {
	public void isValidUserPoint(int price, Long balance) {
		if (price > balance) {
			throw new BaseException(NOT_ENOUGH_POINT);
		}
	}

	// 서비스에 입장한 후 10분이 지나도록 결제도 안하고 있는지 확인
	public Boolean userActiveTimeValidator(List<User> workingUsers, LocalDateTime now) {

		if (!workingUsers.isEmpty()) {
			for (User user : workingUsers) {
				if (now.isAfter(user.getUpdatedAt().plusMinutes(10))) {
					return true;
				}
			}
		}
		return false;
	}

	// 서비스에 입장한 후 10분이 지나도록 결제도 안하고 있다면 내보내준다
	// 서비스를 이용중인 유저가 100명보다 적다면 plusUsersNum++
	public int userSchedulerValidator(List<User> workingUsers, LocalDateTime now, int plusUsersNum) {
		int temp = 0;

		if (!workingUsers.isEmpty()) {
			for (User user : workingUsers) {
				// user의 status를 Done으로 변경
				if (now.isAfter(user.getUpdatedAt().plusMinutes(10))) {
					user.setDone();
					temp++;
				}
			}
		}
		int num = workingUsers.size() - temp;   // Done으로 변경되지 않은 사용자 수 ( 실제 사용자 수 )

		if (num < 100) {
			plusUsersNum = 100 - num;
		}

		// 서비스에 입장할 수 있는 유저의 총 수가 100을 초과하지 않도록 보장
		return Math.min(plusUsersNum, 100);
	}

	public void validUser(User user1, User user2) {
		if (user1.getId() != user2.getId()) {
			throw new BaseException(NOT_VALID_USER);
		}
	}
}
