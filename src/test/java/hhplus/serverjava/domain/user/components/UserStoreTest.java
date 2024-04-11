package hhplus.serverjava.domain.user.components;

import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserStoreRepository;
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
public class UserStoreTest {

    @Mock
    UserStoreRepository userStoreRepository;

    @InjectMocks
    UserStore userStore;

    @DisplayName("테스트")
    @Test
    void test() {
        //given
        Long testId = 1L;
        User user = new User(testId, 500L);

        when(userStoreRepository.save(user)).thenReturn(user);

        //when
        User result = userStore.save(user);

        //then
        assertNotNull(result);
        assertEquals(result.getPoint(), user.getPoint());
    }

}
