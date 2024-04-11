package hhplus.serverjava.api.usecase.payment;

import hhplus.serverjava.api.payment.response.PostPayRes;
import hhplus.serverjava.api.payment.usecase.PaymentUseCase;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.payment.components.PaymentStore;
import hhplus.serverjava.domain.payment.entity.Payment;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentUseCaseTest {

    @Mock
    PaymentStore paymentStore;

    @InjectMocks
    PaymentUseCase paymentUseCase;

    @DisplayName("결제 테스트")
    @Test
    void test() {
        //given
        Long Id = 1L;
        Long payAmount = 50000L;

        User user =new User(Id, 500L);

        Concert concert = Concert.builder()
                .id(Id)
                .name("마크툽 콘서트")
                .artist("마크툽")
                .build();

        ConcertOption concertOption = ConcertOption.builder()
                .id(Id)
                .concertAt(LocalDateTime.now())
                .build();

        Seat seat = Seat.builder()
                .id(Id)
                .price(50000)
                .seatNum(15)
                .build();

        Reservation reservation = Reservation.builder()
                .seatNum(seat.getSeatNum())
                .concertArtist(concert.getArtist())
                .concertName(concert.getName())
                .reservedPrice(seat.getPrice())
                .concertAt(concertOption.getConcertAt())
                .user(user)
                .seat(seat)
                .build();

        Payment payment = Payment.builder()
                .id(Id)
                .payAmount(payAmount)
                .reservation(reservation)
                .build();

        when(paymentStore.save(payment)).thenReturn(payment);

        //when
        PostPayRes result = paymentUseCase.execute(reservation.getId());

        //then
        assertNotNull(result);
        assertEquals(result.getId(), payment.getId());
        assertEquals(result.getPayAmount(), payment.getPayAmount());
        assertEquals(result.getReservationId(), reservation.getId());
    }
}
