package hhplus.serverjava.domain.user.repository;

import hhplus.serverjava.domain.user.entity.User;

import java.util.List;

public interface UserReaderRepository {

    User findUser(Long userId);

    List<User> findUsersByStatus(User.State state);

}
