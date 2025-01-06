package backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Coordinates implements Comparable<Coordinates> {

    @Column(name = "x", nullable = false)
    private int x;

    @Column(name = "y", nullable = false)
    private Integer y;

    @Override
    public int compareTo(Coordinates o) {
        if (this.x != o.x) return Integer.compare(this.x, o.x);
        return Integer.compare(this.y, o.y);
    }
}

