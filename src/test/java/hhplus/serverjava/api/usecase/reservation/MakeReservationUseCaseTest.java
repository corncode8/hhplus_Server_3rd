package hhplus.serverjava.api.usecase.reservation;

import hhplus.serverjava.api.dto.response.reservation.PostReservationRes;
import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionReader;
import hhplus.serverjava.domain.reservation.components.ReservationStore;
import hhplus.serverjava.domain.seat.components.SeatReader;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.jboss.logging.Logger;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.when;


@SpringBootTest
public class MakeReservationUseCaseTest {

    @Autowired
    ConcertOptionReader concertOptionReader;
    @Autowired
    SeatReader seatReader;
    @Autowired
    ReservationStore reservationStore;
    @Autowired
    MakeReservationUseCase makeReservationUseCase;

    private Logger log = Logger.getLogger(MakeReservationUseCaseTest.class);

    /*
    * 예약 로직 테스트
    * 낙관적 락
    */
    @DisplayName("낙관적 락 테스트") // TODO: ING
    @Test
    void optimistic_lock_test() throws InterruptedException, ExecutionException {
        //given
        ExecutorService executor = Executors.newFixedThreadPool(50);    // 50명이라고 가정
        List<CompletableFuture> futures = new ArrayList<>();

        AtomicInteger successCnt = new AtomicInteger(0);
        AtomicInteger failCnt = new AtomicInteger(0);

        int reservedAmount = 50000;
        final Long concertId = 1L;
        LocalDateTime targetDate = LocalDateTime.now().plusDays(1);
        Concert concert = Concert.builder()
                .id(concertId)
                .name("마크툽 콘서트")
                .artist("마크툽")
                .build();

        for (int i = 0; i < 50; i++) {
            int seatNum = i + 1;

            User user = User.builder()
                    .id(i + 1L)
                    .point(i + 500L)
                    .build();
            Seat seat = Seat.builder()
                    .id(i + 1L)
                    .price(reservedAmount)
                    .seatNum(seatNum)
                    .build();

            when(seatReader.findAvailableSeat(concertId, targetDate, Seat.State.AVAILABLE, seatNum)).thenReturn(seat);
            when(concertOptionReader.findConcert(concertId)).thenReturn(concert);

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    PostReservationRes result = makeReservationUseCase.makeReservation(user, concertId, targetDate, seatNum, reservedAmount);
                    successCnt.incrementAndGet();
                } catch (BaseException e) {
                    log.error("BaseException : {}", e);
                    failCnt.incrementAndGet();
                }
            }, executor);
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();

        //then
        System.out.println(successCnt);
    }


}
