package hhplus.serverjava.domain.concert.components;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.EMPTY_CONCERT;
import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FIND_CONCERT;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concert.repository.ConcertReaderRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConcertReader {

	private final ConcertReaderRepository concertReaderRepository;

	public Concert findConcert(Long concertId) {
		return concertReaderRepository.findConcert(concertId)
			.orElseThrow(() -> new BaseException(NOT_FIND_CONCERT));
	}

	@Cacheable(value = "concertList", key = "#state", cacheManager = "cacheManager")
	public List<Concert> findConcertList(Concert.State state) {
		List<Concert> concertList = concertReaderRepository.findConcertList(state);

		if (concertList.isEmpty()) {
			throw new BaseException(EMPTY_CONCERT);
		}

		return concertReaderRepository.findConcertList(state);
	}
}
