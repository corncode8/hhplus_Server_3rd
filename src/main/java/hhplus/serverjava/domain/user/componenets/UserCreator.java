package hhplus.serverjava.domain.user.componenets;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserCreator {

	public static User create(String name, Long point, LocalDateTime updatedAt) {
		return User.create(name, point, updatedAt);
	}
}
