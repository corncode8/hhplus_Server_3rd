package hhplus.serverjava.domain.payment.components;

import hhplus.serverjava.domain.payment.components.PaymentStore;
import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.payment.reservation.PaymentStoreRepository;
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
public class PaymentStoreTest {

    @Mock
    PaymentStoreRepository paymentStoreRepository;

    @InjectMocks
    PaymentStore paymentStore;

    @DisplayName("save테스트")
    @Test
    void saveTest() {
        //given
        Long testId = 1L;

        Payment payment = Payment.builder()
                .id(testId)
                .payAmount(5000L)
                .build();
        when(paymentStoreRepository.save(payment)).thenReturn(payment);

        //when
        Payment result = paymentStore.save(payment);

        //then
        assertNotNull(result);
        assertEquals(result.getPayAmount(), payment.getPayAmount());
    }

}
