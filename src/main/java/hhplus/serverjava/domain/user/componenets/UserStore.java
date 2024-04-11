package hhplus.serverjava.domain.user.componenets;

import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStore {

    private final UserStoreRepository userStoreRepository;

    public User save(User user) {
        return userStoreRepository.save(user);
    }

    // 서비스에 입장한 후 10분이 지나도록 결제도 안하고 있다면 내보내준다
    // 서비스를 이용중인 유저가 100명보다 적다면 plusUsersNum++
    public int UserValidator(List<User> workingUsers, LocalDateTime now, int plusUsersNum) {
        int temp = 0;

        if (!workingUsers.isEmpty()) {
            for (User user : workingUsers) {
                // user의 status를 Done으로 변경
                if (now.isAfter(user.getUpdatedAt().plusMinutes(10))) {
                    user.setDone();
                    temp++;
                }
            }
        }
        int num = workingUsers.size() - temp;   // Done으로 변경되지 않은 사용자 수 ( 실제 사용자 수 )

        if (num < 100) {
            plusUsersNum = 100 - num;
        }

        // 서비스에 입장할 수 있는 유저의 총 수가 100을 초과하지 않도록 보장
        return Math.min(plusUsersNum, 100);
    }

    // plusUsersNum의 수만큼 status를 Processing으로 변경
    public void enterService(List<User> waitUsers, int plusUsersNum) {

        // 유저의 status가 변한 시간을 정렬
        waitUsers = waitUsers.stream()
                .sorted(Comparator.comparing(user -> user.getUpdatedAt()))
                .collect(Collectors.toList());

        for (int i = 0; i < plusUsersNum; i++) {
            User user = waitUsers.get(i);
            user.setProcessing();
        }
    }

    // 대기 번호 확인
    // 서비스를 이용중인 유저가 90명보다 적을 경우 바로 processing
    public Long getUserNum(User user, List<User> userUpdAscList) {

        // updatedAt 오름차순으로 정렬
        Collections.sort(userUpdAscList, Comparator.comparing(u -> u.getUpdatedAt()));

        // 가장 마지막에 서비스에 입장한 유저
        User recentlyUpdUser = userUpdAscList.get(userUpdAscList.size() -1);

        // 조회하려는 유저의 Id - 가장 마지막에 서비스에 입장한 유저Id = 대기번호
        Long userNum = user.getId() - recentlyUpdUser.getId();


        // 서비스를 이용중인 유저가 90명보다 적을 경우 바로 processing
        if (userUpdAscList.size() < 90) {
            user.setProcessing();
            return 0L;
        }

        return userNum;
    }
}
