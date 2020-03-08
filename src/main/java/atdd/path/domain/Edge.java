package atdd.path.domain;

import atdd.path.application.dto.EdgeRequestView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Entity
public class Edge {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "edge_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "station_id", updatable = false, insertable = false)
    private Station source;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "station_id", updatable = false, insertable = false)
    private Station target;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    private int distance;

    private int timeToTake;

    public Edge() {
    }

    public Edge(Station source, Station target, Line line, int distance, int timeToTake) {
        this.source = source;
        this.target = target;
        this.line = line;
        this.distance = distance;
        this.timeToTake = timeToTake;
    }

    @Builder
    public Edge(Long id, Station source, Station target, Line line, int distance, int timeToTake) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.line = line;
        this.distance = distance;
        this.timeToTake = timeToTake;
    }


    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public Line getLine() {
        return line;
    }

    public int getDistance() {
        return distance;
    }

    public int getTimeToTake() {
        return timeToTake;
    }

    public static Edge of(Station source, Station target) {
        return Edge.builder()
                .source(source)
                .target(target)
                .build();
    }


    public static Edge of(EdgeRequestView requestView) {
        return Edge.builder()
                .source(requestView.getSource())
                .target(requestView.getTarget())
                .line(requestView.getLine())
                .distance(requestView.getDistance())
                .timeToTake(requestView.getTimeToTake())
                .build();
    }

    public boolean hasStation(Station station) {
        return source.equals(station) || target.equals(station);
    }
}
