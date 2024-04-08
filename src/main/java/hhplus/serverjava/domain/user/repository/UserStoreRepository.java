package hhplus.serverjava.domain.user.repository;

import hhplus.serverjava.domain.user.entity.User;

public interface UserStoreRepository {

    User save(User user);

}
