package hhplus.serverjava.domain.user.repository;

import hhplus.serverjava.domain.user.entity.User;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserReaderRepository {

    Optional<User> findUser(Long userId);

    Optional<User> findByIdWithLock(Long id);

    List<User> findUsersByStatus(User.State state);

}
