package hhplus.serverjava.domain.user.components;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.domain.user.componenets.UserValidator;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {
	@InjectMocks
	private UserValidator userValidator;

	// TODO: 테스트 수정
	// @DisplayName("isValidUserPoint 테스트")
	// @Test
	// void isValidUserPointTest() {
	// 	//given
	// 	int price = 5000;
	// 	Long balance = 50000L;
	//
	// 	//when & then
	// 	// 잔액이 많은 경우
	// 	userValidator.isValidUserPoint(price, balance);
	//
	// 	// 잔액 부족 Exception
	// 	BaseException exception = assertThrows(BaseException.class,
	// 		() -> userValidator.isValidUserPoint(500000, balance));
	// 	assertEquals(NOT_ENOUGH_POINT.getMessage(), exception.getMessage());
	//
	// }
}
