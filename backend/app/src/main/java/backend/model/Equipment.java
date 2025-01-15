package backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import backend.DTO.EquipmentDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "equipment")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String description;

    public EquipmentDTO toDTO() {
        return EquipmentDTO.builder()
            .name(name)
            .description(description)
            .build();
    }
}
