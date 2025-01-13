package backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "characters")
@Data
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 256)
    private String name;

    @Column(nullable = false, length = 256)
    private String class_name;

    @Column(nullable = false, length = 256)
    private String background;

    @Column(nullable = false, length = 256)
    private String alignment;

    @Embedded
    private Abilities abilityScores;

    @Embedded
    private Skills skills;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "character_id")
    private List<Equipment> equipment;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "character_id")
    private List<FeatureAndTrait> featuresAndTraits;

    @Embedded
    private Personality personality;

    @ElementCollection
    @CollectionTable(name = "goals", joinColumns = @JoinColumn(name = "character_id"))
    @Column(name = "goal")
    private List<String> goals;

    public static Character fromJson(String jsonCharacter) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonCharacter, Character.class);
    }
}
