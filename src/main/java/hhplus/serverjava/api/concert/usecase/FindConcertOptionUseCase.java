package hhplus.serverjava.api.concert.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hhplus.serverjava.api.reservation.response.GetDateResponse;
import hhplus.serverjava.domain.concertoption.components.ConcertOptionReader;
import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FindConcertOptionUseCase {

	private final ConcertOptionReader concertOptionReader;

	// 예약 가능 일자 API
	public GetDateResponse execute(Long concertId) {

		// concertId로 콘서트 옵션 List
		List<ConcertOption> concertOption = concertOptionReader.findConcertOptionList(concertId);

		return new GetDateResponse(concertOption);
	}

}
