package backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class Skills {
    private int acrobatics;
    private int athletics;
    private int insight;
    private int intimidation;
    private int nature;
    private int perception;
    private int survival;
}
