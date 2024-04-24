package hhplus.serverjava.domain.seat.components;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hhplus.serverjava.domain.seat.entity.Seat;
import hhplus.serverjava.domain.seat.repository.SeatStoreRepository;

@ExtendWith(MockitoExtension.class)
public class SeatStoreTest {

	@Mock
	SeatStoreRepository seatStoreRepository;
	@InjectMocks
	SeatStore seatStore;

	@DisplayName("테스트")
	@Test
	void test() {
		//given
		Long testId = 1L;
		Seat seat = new Seat(testId, 50, 50000);

		when(seatStoreRepository.save(seat)).thenReturn(seat);

		//when
		Seat result = seatStore.save(seat);

		//then
		assertNotNull(result);
		assertEquals(result.getSeatNum(), seat.getSeatNum());
		assertEquals(result.getPrice(), seat.getPrice());
	}

}
