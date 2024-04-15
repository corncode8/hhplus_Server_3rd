package hhplus.serverjava.api.usecase.user;

import hhplus.serverjava.api.user.response.GetUserResponse;
import hhplus.serverjava.api.user.usecase.GetWaitNumUseCase;
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
public class GetWaitNumIntegrationTest {

    @Autowired
    private UserStore userStore;
    @Autowired
    private GetWaitNumUseCase getWaitNumUseCase;

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

    @DisplayName("대기열 확인 테스트")
    @Test
    void test() {
        //given
        Long userId1 = 50L;
        Long userId2 = 120L;
        Long userId3 = 150L;

        //when
        GetUserResponse retult1 = getWaitNumUseCase.execute(userId1);
        GetUserResponse retult2 = getWaitNumUseCase.execute(userId2);
        GetUserResponse retult3 = getWaitNumUseCase.execute(userId3);

        //then
        assertNotNull(retult1); assertNotNull(retult2); assertNotNull(retult3);

        // 이미 입장한 유저
        assertEquals(-50, retult1.getListNum());

        // 마지막에 입장한 유저의 ID는 100번 이므로 120 - 100 = 20
        assertEquals(20, retult2.getListNum());

        // 150 -  마지막에 입장한 유저 ID = 50
        assertEquals(50, retult3.getListNum());
    }
}
