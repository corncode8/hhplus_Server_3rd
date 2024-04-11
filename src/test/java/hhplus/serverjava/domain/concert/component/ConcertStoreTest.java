package hhplus.serverjava.domain.concert.component;

import hhplus.serverjava.domain.concert.components.ConcertStore;
import hhplus.serverjava.domain.concert.entity.Concert;
import hhplus.serverjava.domain.concert.repository.ConcertStoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConcertStoreTest {

    @Mock
    ConcertStoreRepository concertStoreRepository;
    @InjectMocks
    ConcertStore concertStore;

    @DisplayName("save테스트")
    @Test
    void saveTest() {
        //given
        Long concertId = 1L;
        String concertName = "MAKTUB CONCERT";
        String artist = "MAKTUB";

        Concert concert = new Concert(concertId, concertName, artist);

        when(concertStoreRepository.save(concert)).thenReturn(concert);

        //when
        Concert result = concertStore.save(concert);

        //then
        assertNotNull(result);
        assertEquals(result.getName(), concert.getName());
        assertEquals(result.getArtist(), concert.getArtist());
    }

}
