package hhplus.serverjava.domain.user.componenets;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import hhplus.serverjava.domain.user.entity.User;
import hhplus.serverjava.domain.user.repository.UserStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStore {

	private final UserStoreRepository userStoreRepository;

	public User save(User user) {
		return userStoreRepository.save(user);
	}

	// plusUsersNum의 수만큼 status를 Processing으로 변경
	public void enterService(List<User> waitUsers, int plusUsersNum) {

		log.info("plusUsersNum : {}", plusUsersNum);
		// 유저의 status가 변한 시간을 정렬
		waitUsers = waitUsers.stream()
			.sorted(Comparator.comparing(user -> user.getUpdatedAt()))
			.collect(Collectors.toList());

		for (int i = 0; i < Math.min(plusUsersNum, waitUsers.size()); i++) {
			User user = waitUsers.get(i);
			user.setProcessing();
		}
	}

	// 대기 번호 확인
	// 서비스를 이용중인 유저가 90명보다 적을 경우 바로 processing
	public Long getUserNum(User user, List<User> userUpdAscList) {

		Long userNum = 0L;

		if (!userUpdAscList.isEmpty()) {
			// updatedAt 오름차순으로 정렬
			Collections.sort(userUpdAscList, Comparator.comparing(u -> u.getUpdatedAt()));

			// 가장 마지막에 서비스에 입장한 유저
			User recentlyUpdUser = userUpdAscList.get(userUpdAscList.size() - 1);

			// 조회하려는 유저의 Id - 가장 마지막에 서비스에 입장한 유저Id = 대기번호
			userNum = user.getId() - recentlyUpdUser.getId();
		} else {
			// 현재 이용중인 유저가 Empty라면 processiong + 대기번호 0L return
			user.setProcessing();
			return 0L;
		}

		// 서비스를 이용중인 유저가 90명보다 적을 경우 바로 processing
		if (userUpdAscList.size() < 90) {
			log.info(user.getId() + "번 유저 상태 PROCESSING으로 변경");
			user.setProcessing();
			return 0L;
		}

		return userNum;
	}
}
