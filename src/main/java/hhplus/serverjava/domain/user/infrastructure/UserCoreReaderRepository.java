package hhplus.serverjava.domain.user.infrastructure;

import hhplus.serverjava.common.exceptions.BaseException;
import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserReaderRepository;
import lombok.RequiredArgsConstructor;

import static hhplus.serverjava.common.response.BaseResponseStatus.*;

@RequiredArgsConstructor
public class UserCoreReaderRepository implements UserReaderRepository {

    private UserJPARepository userJPARepository;

    @Override
    public User findUser(Long userId) {
         return userJPARepository.findById(userId)
                 .orElseThrow(() -> new BaseException(NOT_FIND_USER));
    }
}
