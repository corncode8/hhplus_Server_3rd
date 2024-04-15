package hhplus.serverjava.api.usecase;

import hhplus.serverjava.api.user.response.GetTokenResponse;
import hhplus.serverjava.api.user.usecase.GetTokenUseCase;
import hhplus.serverjava.api.util.jwt.JwtService;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("dev")
public class WaitIntegrationTest {

    // 토큰을 생성하고 대기열에 접근 통합 테스트

    @Autowired
    private UserStore userStore;
    @Autowired
    private UserReader userReader;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private GetTokenUseCase getTokenUseCase;

    // 150명 유저 생성 = 100명 -> 현재 서비스 이용중인 유저, 50명 -> 대기중인 유저
    @BeforeEach
    void setUp() {
        for (int i = 0; i < 150; i++) {
            User user = User.builder()
                    .name("testUser" + i)
                    .point(0L)
                    .updatedAt(LocalDateTime.now())
                    .build();
            if (i < 100) {
                user.setProcessing();
            }
            userStore.save(user);
        }
    }

    // 신규 유저 토큰 생성 후 대기번호 확인
    @DisplayName("신규 유저 토큰 생성 테스트")
    @Test
    void createTokenTest() {
        //given
        String testName = "new_User";

        //when
        // 토큰 생성 후 대기번호, 유저 Status return
        GetTokenResponse result = getTokenUseCase.execute(testName);

        //then
        assertNotNull(result);

        // 새로 생성된 유저는 WATING 상태
        assertEquals(User.State.WAITING, result.getState());

        // 해당 유저의 순번은 51번이어야 함.
        assertEquals(51, result.getListNum());

    }

}
