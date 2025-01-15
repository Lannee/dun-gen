package backend.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Embeddable
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Personality {

    @ElementCollection
    private List<String> traits;

    @Column(nullable = false)
    private String backstory;
}
