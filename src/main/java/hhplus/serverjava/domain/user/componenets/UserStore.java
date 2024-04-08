package hhplus.serverjava.domain.user.componenets;

import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStore {

    private final UserStoreRepository userStoreRepository;

    public User save(User user) {
        return userStoreRepository.save(user);
    }

}
