package hhplus.serverjava.domain.concert.component;

import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.domain.concert.components.ConcertReader;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concert.repository.ConcertReaderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.EMPTY_CONCERT;
import static hhplus.serverjava.api.util.response.BaseResponseStatus.NOT_FIND_CONCERT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConcertReaderTest {

    @Mock
    ConcertReaderRepository concertReaderRepository;

    @InjectMocks
    ConcertReader concertReader;

    @DisplayName("findConcert 테스트")
    @Test
    void findConcertTest() {
        //given
        Long concertId = 1L;
        String concertName = "MAKTUB CONCERT";
        String artist = "MAKTUB";

        Concert concert = new Concert(concertId, concertName, artist);

        when(concertReaderRepository.findConcert(concertId)).thenReturn(Optional.of(concert));

        //when
        Concert result = concertReader.findConcert(concertId);

        //then
        assertNotNull(result);
        assertEquals(result.getName(), concert.getName());
        assertEquals(result.getArtist(), concert.getArtist());
    }

    @DisplayName("findConcert_NotFound_테스트")
    @Test
    void findConcertNotFoundTest() {
        // given
        Long concertId = 1L;
        when(concertReaderRepository.findConcert(concertId)).thenReturn(Optional.empty());

        // when & then
        BaseException exception = assertThrows(BaseException.class, () -> concertReader.findConcert(concertId));
        assertEquals(NOT_FIND_CONCERT.getMessage(), exception.getMessage());
    }

    @DisplayName("findConcertList 테스트")
    @Test
    void findConcertListTest() {
        //given
        Concert.State state = Concert.State.SHOWING;

        List<Concert> concertList = Arrays.asList(
                new Concert(1L, "MAKTUB CONCERT", "MAKTUB"),
                new Concert(2L, "MAKTUB CONCERT", "MAKTUB"),
                new Concert(3L, "MAKTUB CONCERT", "MAKTUB"),
                new Concert(4L, "MAKTUB CONCERT", "MAKTUB"),
                new Concert(5L, "MAKTUB CONCERT", "MAKTUB"),
                new Concert(6L, "MAKTUB CONCERT", "MAKTUB"),
                new Concert(7L, "MAKTUB CONCERT", "MAKTUB"),
                new Concert(8L, "MAKTUB CONCERT", "MAKTUB"),
                new Concert(9L, "MAKTUB CONCERT", "MAKTUB"),
                new Concert(10L, "MAKTUB CONCERT", "MAKTUB")
        );

        when(concertReaderRepository.findConcertList(state)).thenReturn(concertList);

        //when
        List<Concert> result = concertReader.findConcertList(state);

        //then
        assertNotNull(result);
        assertEquals(result.size(), concertList.size());
    }

    @DisplayName("findConcertList_Empty_테스트")
    @Test
    void findConcertListNullTest() {
        //given
        Concert.State state = Concert.State.SHOWING;

        List<Concert> concertList = new ArrayList<>();

        when(concertReaderRepository.findConcertList(state)).thenReturn(concertList);

        //when & then
        BaseException exception = assertThrows(BaseException.class, () -> concertReader.findConcertList(state));
        assertEquals(EMPTY_CONCERT.getMessage(), exception.getMessage());
    }

}
