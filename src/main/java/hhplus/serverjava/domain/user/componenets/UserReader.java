package hhplus.serverjava.domain.user.componenets;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FIND_USER;

@Component
@RequiredArgsConstructor
public class UserReader {

    private final UserReaderRepository userReaderRepository;

    public User findUser(Long userId) {
        return userReaderRepository.findUser(userId)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
    }

    public User findByIdWithLock(Long id) {
        return userReaderRepository.findByIdWithLock(id)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
    }

    public List<User> findUsersByStatus(User.State state) {
        List<User> users = userReaderRepository.findUsersByStatus(state);

//        if (users.isEmpty()) {
//            throw new BaseException(NOT_FIND_USER);
//        }

        return userReaderRepository.findUsersByStatus(state);
    }


}
