package hhplus.serverjava.api.usecase.jwt;

import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class IsValidatedTokenUseCase {

    private final UserReader userReader;

    public User execute(Long userId) {
        return userReader.findUser(userId);
    }
}
