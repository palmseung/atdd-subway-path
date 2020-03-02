package atdd.path.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
public class Station {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    @Builder
    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}

