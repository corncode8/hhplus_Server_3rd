package hhplus.serverjava.api.usecase.user;

import hhplus.serverjava.api.user.response.GetTokenResponse;
import hhplus.serverjava.api.user.usecase.GetTokenUseCase;
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
public class GetTokenUseCaseTest {

    @Mock
    UserReader userReader;
    @Mock
    JwtService jwtService;

    @InjectMocks
    GetTokenUseCase getTokenUseCase;

    @DisplayName("토큰 생성 API 테스트")
    @Test
    void test() {
        //given
        Long userId = 1L;
        String username = "TestUser";
        String jwt = "jfnherjkfera-Test-Token";

        when(jwtService.createJwt(userId)).thenReturn(jwt);

        //when
        GetTokenResponse result = getTokenUseCase.execute(username);

        // then
        assertNotNull(result);
        assertEquals(jwt, result.getToken());

    }
}
