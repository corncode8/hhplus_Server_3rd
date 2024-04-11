package hhplus.serverjava.domain.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @DisplayName("setWaiting테스트")
    @Test
    void setWaitingTest() {
        //given
        Long testId = 1L;
        User user = new User(testId, 500L);

        //when
        LocalDateTime testDateTime = LocalDateTime.now();
        user.setWaiting();

        //then
        assertEquals(User.State.WAITING, user.getStatus());
        assertEquals(testDateTime, user.getUpdatedAt());
    }

    @DisplayName("setProcessing테스트")
    @Test
    void setProcessingTest() {
        //given
        Long testId = 1L;
        User user = new User(testId, 500L);

        //when
        LocalDateTime testDateTime = LocalDateTime.now();
        user.setProcessing();

        //then
        assertEquals(User.State.PROCESSING, user.getStatus());
        assertEquals(testDateTime, user.getUpdatedAt());
    }

    @DisplayName("setDone테스트")
    @Test
    void setDoneTest() {
        //given
        Long testId = 1L;
        User user = new User(testId, 500L);

        //when
        LocalDateTime testDateTime = LocalDateTime.now();
        user.setDone();

        //then
        assertEquals(User.State.DONE, user.getStatus());
        assertEquals(testDateTime, user.getUpdatedAt());
    }

    @DisplayName("sumPoint테스트")
    @Test
    void sumPointTest() {
        //given
        Long testId = 1L;
        User user = new User(testId, 500L);

        //when
        Long testPoint = 500L;
        user.sumPoint(testPoint);

        //then
        assertEquals(500 + 500, user.getPoint());
    }
}
