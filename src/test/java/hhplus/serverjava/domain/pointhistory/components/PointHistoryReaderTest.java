package hhplus.serverjava.domain.pointhistory.components;

import hhplus.serverjava.api.support.exceptions.BaseException;
import hhplus.serverjava.domain.pointhistory.entity.PointHistory;
import hhplus.serverjava.domain.pointhistory.repository.PointHistoryReaderRepository;
import hhplus.serverjava.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static hhplus.serverjava.api.support.response.BaseResponseStatus.NOT_FIND_POINT_LIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PointHistoryReaderTest {

    @Mock
    PointHistoryReaderRepository pointHistoryReaderRepository;

    @InjectMocks
    PointHistoryReader pointHistoryReader;

    @DisplayName("readList테스트")
    @Test
    void readListTest() {
        //given
        Long testId = 1L;
        User user = new User(testId, 500L);

        List<PointHistory> pointHistoryList = Arrays.asList(
                new PointHistory(1L, user, PointHistory.State.CHARGE, 100L),
                new PointHistory(2L, user, PointHistory.State.CHARGE, 200L),
                new PointHistory(3L, user, PointHistory.State.CHARGE, 300L),
                new PointHistory(4L, user, PointHistory.State.CHARGE, 400L),
                new PointHistory(5L, user, PointHistory.State.CHARGE, 500L)
        );

        when(pointHistoryReaderRepository.readList(testId)).thenReturn(pointHistoryList);

        //when
        List<PointHistory> result = pointHistoryReader.readList(testId);

        //then
        assertNotNull(result);
        assertEquals(result.size(), pointHistoryList.size());
    }

    @DisplayName("readListEmpty테스트")
    @Test
    void readListEmptyTest() {
        //given
        Long testId = 1L;

        //when & then
        BaseException exception = assertThrows(BaseException.class, () -> pointHistoryReader.readList(testId));
        assertEquals(NOT_FIND_POINT_LIST.getMessage(), exception.getMessage());
    }

}
