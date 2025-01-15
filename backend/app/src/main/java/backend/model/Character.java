package backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.DTO.CharacterDTO;
import backend.DTO.PersonalityDTO;

@Entity
@Table(name = "characters")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private Integer experience;

    @Column(nullable = false)
    private String class_name;

    @Column(nullable = false)
    private String race;

    @Column(nullable = false)
    private String background;

    @Column(nullable = false)
    private String alignment;

    @Embedded
    private Abilities ability_scores;

    @Embedded
    private Skills skills;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "character_id")
    private List<Equipment> equipment;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "character_id")
    private List<FeatureAndTrait> features_and_traits;

    @Embedded
    private Personality personality;

    @ElementCollection
    @CollectionTable(name = "goals", joinColumns = @JoinColumn(name = "character_id"))
    @Column(name = "goal")
    private List<String> goals;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static Character fromJson(String jsonCharacter) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonCharacter, Character.class);
    }

    public CharacterDTO toDTO() {
        return CharacterDTO.builder()
            .id(id)
            .name(name)
            .level(level)
            .experience(experience)
            .class_name(class_name)
            .race(race)
            .background(background)
            .alignment(alignment)
            .ability_scores(ability_scores)
            .skills(skills)
            .equipment(equipment.stream().map(e -> e.toDTO()).collect(Collectors.toList()))
            .features_and_traits(features_and_traits.stream().map(e -> e.toDTO()).collect(Collectors.toList()))
            .personality(personality.toDTO())
            .goals(goals)
            .build();
    }
}