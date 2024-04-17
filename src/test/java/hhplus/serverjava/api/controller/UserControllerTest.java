package hhplus.serverjava.api.controller;

import hhplus.serverjava.api.user.usecase.GetUserPointUseCase;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserControllerTest.class)
@ActiveProfiles("dev")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GetUserPointUseCase getUserPointUseCase;

    @Autowired
    private UserStore userStore;

    @BeforeEach
    void setUp() {

    }

    @DisplayName("잔액 조회 테스트")
    @Test
    void getPointTest() throws Exception{
        //given
        User user = User.builder()
                .name("testUser")
                .point(5000L)
                .updatedAt(LocalDateTime.now())
                .build();
        userStore.save(user);

        mockMvc.perform(get("/api/point/{userId}/account", user.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.point").value(5000));
    }
}
