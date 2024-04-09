package hhplus.serverjava.api.usecase.point;

import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.infrastructure.UserJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("dev")
public class PointChargeIntegrationTest {

    @Autowired
    private UserReader userReader;

    @Autowired
    private UserStore userStore;
    
    @Autowired
    private UserJPARepository userJPARepository;

    @Autowired
    private UserPointChargeUseCase userPointChargeUseCase;

    /*
    * 테스트 시나리오 ( 낙관적 락 동시성 테스트 )
    * 포인트를 0원 가지고 있는 유저가 55555원 충전 5번을 동시에 요청 -> (55555 * 5)원충전 성공
    */
    @DisplayName("포인트 충전 동시성 테스트")
    @Test
    void 동시성_테스트() throws Exception{
        //given
        User user = saveTestUser();
        
        int threadCnt = 5;
        int point = 55555;
        AtomicInteger cnt = new AtomicInteger(0);

        User newUser = userReader.findUser(user.getId());
        Long userId = newUser.getId();
        
        ExecutorService executorService = Executors.newFixedThreadPool(threadCnt);
        CountDownLatch latch = new CountDownLatch(threadCnt);
        
        //when
        for (int i = 0; i < threadCnt; i++) {
            try {
                executorService.execute(() -> {
                    userPointChargeUseCase.charge(userId, (long)point);
                    cnt.addAndGet(point);
                });
            } finally {
                latch.countDown();
            }
        }
        latch.await();
        
        Thread.sleep(1000);
        

        //then
        User findUser = userReader.findUser(userId);

        // 성공한 횟수 * 충전 포인트 == 충전 포인트 * 스레드 갯수
        assertEquals(cnt.intValue(), point * threadCnt);

        // 최종 유저 포인트 == 충전 포인트 * 스레드 갯수
        assertEquals(findUser.getPoint(), point * threadCnt);

        // 테스트 유저 삭제
        userJPARepository.delete(findUser);

    }

    private User saveTestUser() {
        User user = User.builder()
                .point(0L)
                .name("testUser")
                .updatedAt(LocalDateTime.now())
                .build();
        userStore.save(user);
        return user;
    }
}
