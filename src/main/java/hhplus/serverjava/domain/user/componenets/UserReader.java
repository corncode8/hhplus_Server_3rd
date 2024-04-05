package hhplus.serverjava.domain.user.componenets;

import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {

    private final UserReaderRepository userReaderRepository;

    public User findUser(Long userId) {
        return userReaderRepository.findUser(userId);
    }

}
