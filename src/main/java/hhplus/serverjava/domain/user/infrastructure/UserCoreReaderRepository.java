package hhplus.serverjava.domain.user.infrastructure;

import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class UserCoreReaderRepository implements UserReaderRepository {

    private final UserJPARepository userJPARepository;

    @Override
    public Optional<User> findUser(Long userId) {
         return userJPARepository.findById(userId);
    }

    @Override
    public Optional<User> findByIdWithLock(Long id) {
        return userJPARepository.findByIdWithLock(id);
    }

    @Override
    public List<User> findUsersByStatus(User.State state) {
        return userJPARepository.findUsersByStatus(state);
    }


}

