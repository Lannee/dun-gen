package backend.DTO;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import backend.json.ChapterSerializer;
import backend.json.CoordinatesSerializer;
import backend.json.ZonedDateTimeDeserializer;
import backend.json.ZonedDateTimeSerializer;
import backend.model.AstartesCategory;
import backend.model.Chapter;
import backend.model.Coordinates;
import backend.model.Weapon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class SpaceMarineExistedDTO {
    private Long id;

    private String name;

    @JsonSerialize(using = CoordinatesSerializer.class)
    private Coordinates coordinates;

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime creationDate;

    @JsonSerialize(using = ChapterSerializer.class)
    private Chapter chapter;

    private double health;

    private String achievements;

    private AstartesCategory category;

    private Weapon weaponType;

    private long user_id;

    private boolean editable;
}
