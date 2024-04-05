package hhplus.serverjava.domain.user.componenets;

import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserReaderRepository;
import hhplus.serverjava.domain.user.repository.UserStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserManager {

    private final UserReaderRepository userReaderRepository;
    private final UserStoreRepository userStoreRepository;


    public User findUser(Long userId) {
        return userReaderRepository.findUser(userId);
    }
    public User modify(User user) {
        return userStoreRepository.modify(user);
    }

}
