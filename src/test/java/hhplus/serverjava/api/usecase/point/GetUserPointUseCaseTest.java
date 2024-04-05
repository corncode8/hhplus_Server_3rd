package hhplus.serverjava.api.usecase.point;

import hhplus.serverjava.api.dto.response.user.UserPoint;
import hhplus.serverjava.api.usecase.point.GetUserPointUseCase;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetUserPointUseCaseTest {

    @Mock
    UserReader userReader;

    @InjectMocks
    GetUserPointUseCase getUserPointUseCase;

    @DisplayName("UserPoint Mapper 테스트")
    @Test
    void test() {
        //given
        Long userId = 1L;
        Long point = 500L;
        User user = User.builder()
                .id(userId)
                .point(point)
                .build();

        when(userReader.findUser(userId)).thenReturn(user);

        //when
        UserPoint result = getUserPointUseCase.execute(userId);

        //then
        assertNotNull(result);
        assertEquals(point, result.getPoint());
    }
}
