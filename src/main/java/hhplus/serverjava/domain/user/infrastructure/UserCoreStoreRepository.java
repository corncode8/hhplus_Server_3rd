package hhplus.serverjava.domain.user.infrastructure;

import hhplus.serverjava.common.exceptions.BaseException;
import hhplus.serverjava.common.response.BaseResponseStatus;
import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserStoreRepository;
import lombok.RequiredArgsConstructor;

import static hhplus.serverjava.common.response.BaseResponseStatus.NOT_FIND_USER;

@RequiredArgsConstructor
public class UserCoreStoreRepository implements UserStoreRepository {

    private UserJPARepository userJPARepository;

    @Override
    public User modify(User user) {

        return userJPARepository.save(user);
    }

    @Override
    public User findUser(Long userId) {
        return userJPARepository.findById(userId)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
    }

}
