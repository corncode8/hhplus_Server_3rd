package hhplus.serverjava.api.user.response;

import java.time.LocalDateTime;

import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PointHistoryList {

	private Long id;
	private Long userId;
	private PointHistory.State state;
	private Long amount;
	private LocalDateTime time;

	public PointHistoryList(Long id, Long userId, PointHistory.State state, Long amount, LocalDateTime time) {
		this.id = id;
		this.userId = userId;
		this.state = state;
		this.amount = amount;
		this.time = time;
	}

	public PointHistoryList(PointHistory pointHistory) {
		this.id = pointHistory.getId();
		this.userId = pointHistory.getUser().getId();
		this.state = pointHistory.getType();
		this.amount = pointHistory.getAmount();
		this.time = pointHistory.getCreatedAt();
	}
}
