package hhplus.serverjava.api.controller;

import hhplus.serverjava.api.payment.PaymentController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PaymentControllerTest.class)
@ActiveProfiles("dev")
public class PaymentControllerTest {

    @Autowired
    private PaymentController paymentController;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("결제 테스트")
    @Test
    void paymentTest() {
        //given

        //when

        //then
    }
}
