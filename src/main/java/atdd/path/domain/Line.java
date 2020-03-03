package atdd.path.domain;

import atdd.path.application.dto.LineResponseView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "line_id")
    private Long id;

    private String name;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer intervalTime;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "edge_id")
    private List<Edge> edges = new ArrayList<>();

    public Line() {
    }

    @Builder
    public Line(Long id, String name, LocalTime startTime, LocalTime endTime,
                Integer intervalTime, List<Edge> edges) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.edges = edges;
    }

    public void changeStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void changeEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void changeInterval(int interval) {
        this.intervalTime = interval;
    }

    public static Line of(LineResponseView responseView) {
        return Line.builder()
                .id(responseView.getId())
                .name(responseView.getName())
                .startTime(responseView.getStartTime())
                .endTime(responseView.getEndTime())
                .intervalTime(responseView.getInterval())
                .build();
    }

    private List<Station> getStations(List<Edge> edges) {
        if (edges.size() == 0) {
            return new ArrayList<>();
        }

        List<Station> stations = new ArrayList();
        Station lastStation = findFirstStation(edges);
        stations.add(lastStation);

        while (true) {
            Station nextStation = findNextStationOf(edges, lastStation);
            if (nextStation == null) {
                break;
            }
            stations.add(nextStation);
            lastStation = nextStation;
        }

        return stations;
    }

    private Station findFirstStation(List<Edge> edges) {
        List<Station> sourceStations = edges.stream()
                .map(it -> it.getTarget())
                .collect(Collectors.toList());

        return edges.stream()
                .map(it -> it.getSource())
                .filter(it -> !sourceStations.contains(it))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private Station findNextStationOf(List<Edge> edges, Station firstStation) {
        return edges.stream()
                .filter(it -> firstStation.equals(it.getSource()))
                .map(it -> it.getTarget())
                .findFirst()
                .orElse(null);
    }

    public Edges removeStation(Station station) {
        List<Edge> replaceEdge = this.edges.stream()
                .filter(it -> it.hasStation(station))
                .collect(Collectors.toList());

        if (replaceEdge.size() == 0) {
            throw new RuntimeException();
        }

        List<Edge> newEdges = this.edges.stream()
                .filter(it -> !replaceEdge.contains(it))
                .collect(Collectors.toList());

        if (replaceEdge.size() == 1) {
            this.edges = newEdges;
            return new Edges(newEdges);
        }

        Edge newEdge = Edge.of(getSourceStationOf(station), getTargetStationOf(station));
        newEdges.add(newEdge);

        return new Edges(newEdges);
    }

    private Station getSourceStationOf(Station station) {
        return this.edges.stream()
                .filter(it -> station.equals(it.getTarget()))
                .map(it -> it.getSource())
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private Station getTargetStationOf(Station station) {
        return this.edges.stream()
                .filter(it -> station.equals(it.getSource()))
                .map(it -> it.getTarget())
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public void addEdgeToLine(Edge edge) {
        if(edges == null){
            edges = new ArrayList<>();
        }
        this.edges.add(edge);
//        List<Edge> newEdges = new ArrayList<>();
//
//        newEdges = this.edges.stream().collect(Collectors.toList());
//        newEdges.add(edge);
//        return new Edges(newEdges);
    }

    private Integer sum(List<Edge> replaceEdge) {
        return replaceEdge.stream().map(it -> it.getDistance()).reduce(0, Integer::sum);
    }

    public List<Station> getStations() {
        return getStations(this.edges);
    }
}
