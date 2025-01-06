package backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import backend.DTO.SpaceMarineExistedDTO;
import backend.json.ChapterSerializer;
import backend.json.CoordinatesSerializer;
import backend.json.ZonedDateTimeDeserializer;
import backend.json.ZonedDateTimeSerializer;

import java.time.ZonedDateTime;

@Entity
@Table(name = "space_marines")
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class SpaceMarine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Embedded
    @Column(name = "coordinates")
    @JsonSerialize(using = CoordinatesSerializer.class)
    private Coordinates coordinates;

    @Column(name = "creation_date", nullable = false, updatable = false, columnDefinition = "timestamp with time zone")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime creationDate;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    @JsonSerialize(using = ChapterSerializer.class)
    private Chapter chapter;

    @Column(name = "health", nullable = false)
    private double health;

    @Column(name = "achievements", nullable = false, length = 255)
    private String achievements;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private AstartesCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "weapon_type", nullable = false)
    private Weapon weaponType;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    @JsonIgnore
    private User user;

    // Конструкторы, геттеры и сеттеры

    public SpaceMarine() {
        this.creationDate = ZonedDateTime.now();
    }

    public SpaceMarine(String name, Coordinates coordinates, Chapter chapter, double health, String achievements, AstartesCategory category, Weapon weaponType, User user) {
        this.name = name;
        this.coordinates = coordinates;
        this.chapter = chapter;
        this.health = health;
        this.achievements = achievements;
        this.category = category;
        this.weaponType = weaponType;
        this.creationDate = ZonedDateTime.now();
        this.user = user;
    }

    public SpaceMarineExistedDTO toDTO() {
        return SpaceMarineExistedDTO.builder()
            .id(this.id)
            .name(this.name)
            .coordinates(this.coordinates)
            .chapter(this.chapter)
            .health(this.health)
            .achievements(this.achievements)
            .category(this.category)
            .weaponType(this.weaponType)
            .user_id(this.user.getId())
            .editable(false)
            .build();
    }   
}

