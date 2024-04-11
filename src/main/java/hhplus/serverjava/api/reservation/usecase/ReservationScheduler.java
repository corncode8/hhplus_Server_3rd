package hhplus.serverjava.api.reservation.usecase;

import hhplus.serverjava.domain.reservation.components.ReservationReader;
import hhplus.serverjava.domain.reservation.components.ReservationStore;
import hhplus.serverjava.domain.reservation.entity.Reservation;
import hhplus.serverjava.domain.seat.components.SeatStore;
import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.user.componenets.UserReader;
import hhplus.serverjava.domain.user.componenets.UserStore;
import hhplus.serverjava.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationScheduler {

    private final SeatStore seatStore;
    private final UserStore userStore;
    private final UserReader userReader;
    private final ReservationStore reservationStore;
    private final ReservationReader reservationReader;

    @Scheduled(fixedDelay = 60000)     // 1분마다 실행
    @Transactional
    public void reservationExpired() {
        // 좌석 5분 임시배정(스케줄러)
        // 좌석의 만료시간이 5분이 지났다면 해당 좌석 AVAILABLE로 수정후 예약 CANCELLED로 수정

        // 좌석이 만료된 예약 조회
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> expiredReservaions = reservationReader.findExpiredReservaions(now);

        if (!expiredReservaions.isEmpty()) {
            for (Reservation reservation : expiredReservaions) {
                Seat seat = reservation.getSeat();

                seat.setAvailable();
                reservation.setCancelled();

            }
        }

        // PROCESSING 유저 List
        List<User> workingUsers = userReader.findUsersByStatus(User.State.PROCESSING);

        int plusUsersNum = 0;
        if (!workingUsers.isEmpty()) {
            for (User user : workingUsers) {
                // 서비스에 입장한 후 10분이 지나도록 예약도 안하고 있다면 내보내줌
                if (now.isAfter(user.getUpdatedAt().plusMinutes(10))) {
                    user.setDone();
                    plusUsersNum++;
                }
            }
        }

        // 서비스를 이용중인 유저가 100명보다 적다면
        if (workingUsers.size() < 100) {
            int num = 100 - workingUsers.size();
            plusUsersNum += num;
        }

        // WAIT 유저 List
        List<User> waitUsers = userReader.findUsersByStatus(User.State.WAITING);

        // 유저의 status가 변한 시간을 정렬
        if (!waitUsers.isEmpty()) {
            waitUsers = waitUsers.stream()
                    .sorted(Comparator.comparing(user -> user.getUpdatedAt()))
                    .collect(Collectors.toList());
        }

        // plusUsersNum의 수만큼 status를 Processing으로 변경
        for (int i = 0; i < Math.min(plusUsersNum, waitUsers.size()); i++) {
            User user = waitUsers.get(i);
            user.setProcessing();

        }
    }

}
