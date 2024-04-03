package hhplus.serverjava.domain.user.repository;

import hhplus.serverjava.domain.user.entity.User;

public interface UserStoreRepository {

    User modify(User user);

    User findUser(Long userId);
}
