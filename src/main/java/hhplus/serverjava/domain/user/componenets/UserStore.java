package hhplus.serverjava.domain.user.componenets;

import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStore {

    private final UserStoreRepository userStoreRepository;

    public User modify(User user) {
        return userStoreRepository.modify(user);
    }

    public User findUser(Long userId) {
        return userStoreRepository.findUser(userId);
    }
}
