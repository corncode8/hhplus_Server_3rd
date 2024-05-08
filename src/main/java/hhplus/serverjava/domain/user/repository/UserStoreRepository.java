package hhplus.serverjava.domain.user.repository;

import java.util.Optional;

import hhplus.serverjava.domain.user.entity.User;

public interface UserStoreRepository {
	User save(User user);

	Optional<User> findUser(Long userId);

}
