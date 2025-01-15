package backend.DTO;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.model.Abilities;
import backend.model.Skills;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CharacterDTO {

    private Long id;

    private String name;

    private Integer level;

    private Integer experience;

    private String class_name;

    private String race;

    private String background;

    private String alignment;

    private Abilities ability_scores;

    private Skills skills;

    private List<EquipmentDTO> equipment;

    private List<FeatureAndTraitDTO> features_and_traits;

    private PersonalityDTO personality;

    private List<String> goals;

    private String token;

    public String toJson() {
        try {
            return new ObjectMapper().writer().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
