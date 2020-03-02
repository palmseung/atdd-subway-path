package atdd.path.service;

import atdd.path.application.dto.LineRequestView;
import atdd.path.application.dto.LineResponseView;
import atdd.path.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityExistsException;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
    public static final String LINE_2_NAME = "2호선";
    public static final String STATION_NAME = "사당";
    public static final String STATION_NAME_2 = "방배";
    public static final String STATION_NAME_3 = "서초";
    public static final String STATION_NAME_4 = "강남";
    public static final LocalTime START_TIME = LocalTime.of(5, 00);
    public static final LocalTime END_TIME = LocalTime.of(23, 50);
    public static final int INTERVAL_TIME = 10;
    public Station station1 = Station.builder()
            .name(STATION_NAME)
            .build();
    public Station station2 = Station.builder()
            .name(STATION_NAME_2)
            .build();
    public Station station3 = Station.builder()
            .name(STATION_NAME_3)
            .build();
    public Station station4 = Station.builder()
            .name(STATION_NAME_4)
            .build();
    public Line line = Line.builder()
            .id(1L)
            .name(LINE_2_NAME)
            .startTime(START_TIME)
            .endTime(END_TIME)
            .interval(INTERVAL_TIME)
            .build();
    public LineRequestView requestView = LineRequestView.builder()
            .name(LINE_2_NAME)
            .startTime(START_TIME)
            .endTime(END_TIME)
            .interval(INTERVAL_TIME)
            .build();

    @InjectMocks
    LineService lineService;

    @Mock
    LineRepository lineRepository;

    @Autowired
    EdgeRepository edgeRepository;

    @Mock
    StationRepository stationRepository;

    @Test
    void 지하철노선_등록하기() {
        //given
        given(lineRepository.save(any(Line.class))).willReturn(line);
        given(lineRepository.findByName(anyString())).willReturn(Optional.empty());

        //when
        LineResponseView responseView = lineService.create(requestView);

        //then
        assertThat(responseView.getName()).isEqualTo(LINE_2_NAME);
        assertThat(responseView.getEndTime()).isEqualTo(END_TIME);
    }

    @Test
    void 지하철노선의_시작시간은_종료시간보다_빨라야_한다() {
        //given
        LocalTime newStartTime = LocalTime.of(23, 55);
        line.changeStartTime(newStartTime);
        given(lineRepository.findByName(anyString())).willReturn(Optional.empty());

        //when, then
        assertThrows(IllegalArgumentException.class, () -> {
            lineService.create(LineRequestView.of(line));
        });
    }

    @Test
    void 지하철노선의_배차간격은_영보다_커야한다() {
        //given
        int newInterval = -2;
        line.changeInterval(newInterval);
        given(lineRepository.findByName(anyString())).willReturn(Optional.empty());

        //when, then
        assertThrows(IllegalArgumentException.class, () -> {
            lineService.create(LineRequestView.of(line));
        });
    }

    @Test
    void 이미_등록된_노선은_다시_등록할_수_없다() {
        //given
        given(lineRepository.findByName(anyString())).willReturn(Optional.of(line));

        //when, then
        assertThrows(EntityExistsException.class, () -> {
            lineService.create(LineRequestView.of(line));
        });
    }

    @Test
    void 지하철노선을_삭제한다(){
        //given
        given(lineRepository.findById(anyLong())).willReturn(Optional.of(line));

        //when
        lineService.delete(line.getId());

        //then
        verify(lineRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void 이미_등록된_지하철노선만_삭제_가능하다(){
        //given
        given(lineRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        lineService.delete(line.getId());

        //then
        verify(lineRepository, times(0)).deleteById(anyLong());
    }

//    @Test
//    void 지하철노선에_구간을_추가하기(){
//        //given
//        given(stationRepository.findById(station1.getId())).willReturn(Optional.of(station1));
//        given(stationRepository.findById(station2.getId())).willReturn(Optional.of(station2));
//
//        //when
//        lineService.addEdge(line, station1.getId(), station2.getId(), INTERVAL_TIME);
//
//        //then
//        //verify(edgeRepository, times(1)).save(any(Edge.class));
//        assertThat(line.getEdges().get(0)).isNotNull();
//    }
}
