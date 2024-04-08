package hhplus.serverjava.domain.user.infrastructure;

import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.NOT_FIND_USER;

@Repository
@RequiredArgsConstructor
public class UserCoreStoreRepository implements UserStoreRepository {

    private final UserJPARepository userJPARepository;

    @Override
    public User save(User user) {
        return userJPARepository.save(user);
    }

}
