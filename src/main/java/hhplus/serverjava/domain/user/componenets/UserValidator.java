package hhplus.serverjava.domain.user.componenets;

import hhplus.serverjava.api.util.exceptions.BaseException;
import org.springframework.stereotype.Component;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.NOT_ENOUGH_POINT;

@Component
public class UserValidator {

    public void isValidUserPoint(int price, Long balance) {
        if (price > balance) {
            throw new BaseException(NOT_ENOUGH_POINT);
        }
    }

}
