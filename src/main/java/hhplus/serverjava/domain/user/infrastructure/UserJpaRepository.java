package hhplus.serverjava.domain.user.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hhplus.serverjava.domain.user.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
	List<User> findUsersByStatus(User.State state);

}
