package hhplus.serverjava.api.concert.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.concert.response.GetConcertResponse;
import hhplus.serverjava.domain.concert.components.ConcertReader;
import hhplus.serverjava.domain.concert.entity.Concert;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetConcertListUseCase {

	private final ConcertReader concertReader;

	// 콘서트 조회 API
	public GetConcertResponse execute() {

		// 현재 상영중인 콘서트 List
		List<Concert> concertList = concertReader.findConcertList(Concert.State.SHOWING);

		return new GetConcertResponse(concertList);
	}
}
