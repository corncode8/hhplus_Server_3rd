package hhplus.serverjava.domain.user.repository;

import hhplus.serverjava.domain.user.entity.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserReaderRepository {

    User findUser(Long userId);

    User findByIdWithLock(Long id);

    List<User> findUsersByStatus(User.State state);

}
