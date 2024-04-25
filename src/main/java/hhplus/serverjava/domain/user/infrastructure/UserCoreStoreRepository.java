package hhplus.serverjava.domain.user.infrastructure;

import org.springframework.stereotype.Repository;

import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserStoreRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserCoreStoreRepository implements UserStoreRepository {
	private final UserJpaRepository userJpaRepository;

	@Override
	public User save(User user) {
		return userJpaRepository.save(user);
	}

}
