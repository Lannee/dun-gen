package backend.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Skills {
    private int acrobatics;
    private int athletics;
    private int insight;
    private int intimidation;
    private int nature;
    private int perception;
    private int survival;
}
