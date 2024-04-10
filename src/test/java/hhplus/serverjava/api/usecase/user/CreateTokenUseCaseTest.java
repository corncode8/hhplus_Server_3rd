package hhplus.serverjava.api.usecase.user;

import hhplus.serverjava.api.dto.response.user.GetTokenRes;
import hhplus.serverjava.api.util.jwt.JwtService;
import hhplus.serverjava.domain.user.componenets.UserReader;
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
public class CreateTokenUseCaseTest {

    @Mock
    UserReader userReader;
    @Mock
    JwtService jwtService;

    @InjectMocks
    CreateTokenUseCase createTokenUseCase;

    @DisplayName("토큰 생성 API 테스트")
    @Test
    void test() {
        //given
        Long userId = 1L;
        String username = "TestUser";
        String jwt = "jfnherjkfera-Test-Token";

        when(jwtService.createJwt(userId)).thenReturn(jwt);

        //when
        GetTokenRes result = createTokenUseCase.execute(username);

        // then
        assertNotNull(result);
        assertEquals(jwt, result.getToken());

    }
}
