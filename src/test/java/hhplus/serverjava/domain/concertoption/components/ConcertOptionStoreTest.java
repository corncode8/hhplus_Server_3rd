package hhplus.serverjava.domain.concertoption.components;


import hhplus.serverjava.domain.concertoption.entity.ConcertOption;
import hhplus.serverjava.domain.concertoption.repository.ConcertOptionStoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConcertOptionStoreTest {

    @Mock
    ConcertOptionStoreRepository concertOptionStoreRepository;

    @InjectMocks
    ConcertOptionStore concertOptionStore;

    @DisplayName("save테스트")
    @Test
    void saveTest() {
        //given
        Long testId = 1L;
        LocalDateTime testDateTime = LocalDateTime.now();
        ConcertOption concertOption = new ConcertOption(testId, testDateTime);

        when(concertOptionStoreRepository.save(concertOption)).thenReturn(concertOption);

        //when
        ConcertOption result = concertOptionStore.save(concertOption);

        //then
        assertNotNull(result);
        assertEquals(result.getConcertAt(), concertOption.getConcertAt());
    }

}
