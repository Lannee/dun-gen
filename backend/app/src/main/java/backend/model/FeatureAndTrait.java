package backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import backend.DTO.EquipmentDTO;
import backend.DTO.FeatureAndTraitDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "features_and_traits")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class FeatureAndTrait {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String description;

    public FeatureAndTraitDTO toDTO() {
        return FeatureAndTraitDTO.builder()
            .name(name)
            .description(description)
            .build();
    }
}
