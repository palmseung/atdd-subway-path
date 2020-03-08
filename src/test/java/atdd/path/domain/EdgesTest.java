package atdd.path.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;


public class EdgesTest {
    private List<Edge> edgeList = Arrays.asList(TEST_EDGE, TEST_EDGE_2, TEST_EDGE_3);
    private Edges edges = new Edges(edgeList);

    @Test
    void findFirstStationInEdgesTest(){
        //when
        Station firstStationInEdges = edges.findFirstStation();

        //then
        assertThat(firstStationInEdges.getId()).isEqualTo(TEST_STATION.getId());
    }

    @Test
    void findTargetStationTest(){
        //when
        Station nextStation = edges.findTargetStation(TEST_STATION_3);

        //then
        assertThat(nextStation).isEqualTo(TEST_STATION_4);
    }

    @Test
    void findSourceStationTest() {
        //when
        Station sourceStation = edges.findSourceStation(TEST_STATION_3);

        //then
        assertThat(sourceStation).isEqualTo(TEST_STATION_2);
    }
}
