package hhplus.serverjava.api.usecase.point;

import hhplus.serverjava.api.dto.response.user.UserPoint;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
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
public class UserPointModifyUseCaseTest {

    @Mock
    UserStore userStore;
    @Mock
    UserReader userReader;

    @InjectMocks
    UserPointModifyUseCase userPointModifyUseCase;


    @DisplayName("포인트 충전 테스트")
    @Test
    void test() {
        //given
        Long userId = 1L;
        Long point = 200L;
        User user = User.builder()
                .id(userId)
                .point(point)
                .build();

        when(userReader.findUser(userId)).thenReturn(user);
        when(userStore.save(user)).thenReturn(user);

        Long chargePoint = 500L;
        //when
        UserPoint result = userPointModifyUseCase.charge(userId, chargePoint);

        //then
        assertNotNull(result);
        assertEquals(result.getPoint(), point + chargePoint);
    }
}
