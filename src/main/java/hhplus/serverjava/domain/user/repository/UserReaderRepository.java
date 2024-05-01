package hhplus.serverjava.domain.user.repository;

import java.util.List;
import java.util.Optional;

import hhplus.serverjava.domain.user.entity.User;

public interface UserReaderRepository {
	Optional<User> findUser(Long userId);

	Optional<User> findById(Long id);

	List<User> findUsersByStatus(User.State state);

}
