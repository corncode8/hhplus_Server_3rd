package hhplus.serverjava.api.concert.response;

import hhplus.serverjava.domain.concert.entity.Concert;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConcertInfo {

	private Long concertId;
	private String concertName;
	private String artist;

	public ConcertInfo(Concert concert) {
		this.concertId = concert.getId();
		this.concertName = concert.getName();
		this.artist = concert.getArtist();
	}
}
