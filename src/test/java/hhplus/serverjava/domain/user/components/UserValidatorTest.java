package hhplus.serverjava.domain.user.components;

import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.domain.user.componenets.UserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.NOT_ENOUGH_POINT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

    @InjectMocks
    private UserValidator userValidator;

    @DisplayName("isValidUserPoint 테스트")
    @Test
    void isValidUserPointTest() {
        //given
        int price = 5000;
        Long balance = 50000L;

        //when & then
        // 잔액이 많은 경우
        userValidator.isValidUserPoint(price, balance);

        // 잔액 부족 Exception
        BaseException exception = assertThrows(BaseException.class, () -> userValidator.isValidUserPoint(500000, balance));
        assertEquals(NOT_ENOUGH_POINT.getMessage(), exception.getMessage());

    }
}
