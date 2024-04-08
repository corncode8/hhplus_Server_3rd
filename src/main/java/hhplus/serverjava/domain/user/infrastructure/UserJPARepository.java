package hhplus.serverjava.domain.user.infrastructure;

import hhplus.serverjava.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJPARepository extends JpaRepository<User, Long> {

    List<User> findUsersByStatus(User.State state);

}
