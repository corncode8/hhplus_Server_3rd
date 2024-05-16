package hhplus.serverjava.domain.eventhistory.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EventHistory", indexes = {
	@Index(name = "idx_event_created_at_published", columnList = "createdAt, published")
})
@Getter
@NoArgsConstructor
public class EventHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eventhistory_id", nullable = false, updatable = false)
	private Long id;

	@Column(nullable = false)
	private Boolean published;

	@Column(nullable = false)
	private Actor actor;

	@Column(nullable = false)
	private Long actorId;

	@Column(nullable = false)
	private eventChannel eventChannel;

	@Column(nullable = true)
	private String reason;

	@CreationTimestamp
	@Column(name = "createdAt", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	public void setTruePublished() {
		this.published = true;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public enum eventChannel {
		PAYMENT, RESERVATION, USER
	}

	public enum Actor {
		PAY, MAKE_RESERVATION
	}

	@Builder
	public EventHistory(Boolean published, Actor actor, Long actorId, EventHistory.eventChannel eventChannel) {
		this.published = published;
		this.actor = actor;
		this.actorId = actorId;
		this.eventChannel = eventChannel;
	}
}
