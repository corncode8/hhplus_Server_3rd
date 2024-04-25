package hhplus.serverjava.domain.user.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserReaderRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserCoreReaderRepository implements UserReaderRepository {
	private final UserJpaRepository userJpaRepository;

	@Override
	public Optional<User> findUser(Long userId) {
		return userJpaRepository.findById(userId);
	}

	@Override
	public Optional<User> findByIdWithLock(Long id) {
		return userJpaRepository.findByIdWithLock(id);
	}

	@Override
	public List<User> findUsersByStatus(User.State state) {
		return userJpaRepository.findUsersByStatus(state);
	}

}

