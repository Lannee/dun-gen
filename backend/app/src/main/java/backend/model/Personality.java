package backend.model;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import backend.DTO.PersonalityDTO;


@Embeddable
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Personality {

    @ElementCollection
    private List<String> traits;

    @Column(nullable = false)
    private String backstory;

    public PersonalityDTO toDTO() {
        return PersonalityDTO.builder()
            .traits(traits)
            .backstory(backstory)
            .build();
    }
}
