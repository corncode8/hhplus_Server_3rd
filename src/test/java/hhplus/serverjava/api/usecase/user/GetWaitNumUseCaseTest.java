package hhplus.serverjava.api.usecase.user;

import hhplus.serverjava.api.dto.response.user.GetUserRes;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetWaitNumUseCaseTest {

    @Mock
    UserReader userReader;
    @Mock
    UserStore userStore;

    @InjectMocks
    GetWaitNumUseCase getWaitNumUseCase;

    @DisplayName("대기열 확인 API 테스트")
    @Test
    void test() {
        //given
        Long userId = 105L;
        User testUser = new User(userId, 1000L);
        LocalDateTime now = LocalDateTime.now();

        List<User> mockUser = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Long idx = i + 1L;
            Long point = 500L;

            User user = new User(idx, point);
            user.setUpdatedAt(now);

            mockUser.add(user);
        }

        when(userStore.save(testUser)).thenReturn(testUser);
        when(userReader.findUser(userId)).thenReturn(testUser);
        when(userReader.findUsersByStatus(User.State.PROCESSING)).thenReturn(mockUser);

        //when
        GetUserRes result = getWaitNumUseCase.execute(userId);

        //then
        assertNotNull(result);
        assertEquals(userId - mockUser.size(), 5);

    }

}
