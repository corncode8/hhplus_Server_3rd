package hhplus.serverjava.domain.user.components;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserReaderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FIND_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserReaderTest {

    @Mock
    UserReaderRepository userReaderRepository;

    @InjectMocks
    UserReader userReader;

    @DisplayName("findUser테스트")
    @Test
    void findUserTest() {
        //given
        Long testId = 1L;
        User user = new User(testId, 500L);

        when(userReaderRepository.findUser(testId)).thenReturn(Optional.of(user));

        //when
        User result = userReader.findUser(testId);

        //then
        assertNotNull(result);
        assertEquals(result.getPoint(), user.getPoint());
    }

    @DisplayName("findUser_NotFound_테스트")
    @Test
    void findUserNotFoundTest() {
        //given
        Long testId = 1L;
        User user = new User(testId, 500L);

        when(userReaderRepository.findUser(testId)).thenReturn(Optional.empty());

        //when & then
        BaseException exception = assertThrows(BaseException.class, () -> userReader.findUser(testId));
        assertEquals(NOT_FIND_USER.getMessage(), exception.getMessage());
    }

    @DisplayName("findByIdWithLock테스트")
    @Test
    void findByIdWithLockTest() {
        //given
        Long testId = 1L;
        User user = new User(testId, 500L);

        when(userReaderRepository.findUser(testId)).thenReturn(Optional.of(user));

        //when
        User result = userReader.findUser(testId);

        //then
        assertNotNull(result);
        assertEquals(result.getPoint(), user.getPoint());
    }

    @DisplayName("findByIdWithLock_NotFound_테스트")
    @Test
    void findByIdWithLockNotFoundTest() {
        //given
        Long testId = 1L;
        User user = new User(testId, 500L);

        when(userReaderRepository.findUser(testId)).thenReturn(Optional.empty());

        //when & then
        BaseException exception = assertThrows(BaseException.class, () -> userReader.findUser(testId));
        assertEquals(NOT_FIND_USER.getMessage(), exception.getMessage());
    }

    @DisplayName("findUsersByStatus테스트")
    @Test
    void findUsersByStatusTest() {
        //given
        List<User> userList = Arrays.asList(
                new User(1L, 500L),
                new User(2L, 500L),
                new User(3L, 500L),
                new User(4L, 500L),
                new User(5L, 500L)
        );

        when(userReaderRepository.findUsersByStatus(User.State.WAITING)).thenReturn(userList);

        //when
        List<User> result = userReader.findUsersByStatus(User.State.WAITING);

        //then
        assertNotNull(result);
        assertEquals(result.size(), userList.size());
    }

    @DisplayName("findUsersByStatusEmtpy테스트")
    @Test
    void findUsersByStatusEmptyTest() {
        //given
        List<User> userList = new ArrayList<>();

        when(userReaderRepository.findUsersByStatus(User.State.WAITING)).thenReturn(userList);

        //when & then
        BaseException exception = assertThrows(BaseException.class, () -> userReader.findUsersByStatus(User.State.WAITING));
        assertEquals(NOT_FIND_USER.getMessage(), exception.getMessage());


    }
}
