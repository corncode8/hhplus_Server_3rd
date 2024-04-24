package hhplus.serverjava.domain.concert.repository;

import hhplus.serverjava.domain.concert.entity.Concert;

public interface ConcertStoreRepository {

	Concert save(Concert concert);
}
