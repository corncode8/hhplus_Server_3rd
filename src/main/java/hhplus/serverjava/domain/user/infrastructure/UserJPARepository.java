package hhplus.serverjava.domain.user.infrastructure;

import hhplus.serverjava.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;

public interface UserJPARepository extends JpaRepository<User, Long> {

    List<User> findUsersByStatus(User.State state);


    @Query("select u from User u where u.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    User findByIdWithLock(@Param("id") Long id);

}
