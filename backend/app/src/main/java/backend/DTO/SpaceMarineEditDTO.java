package backend.DTO;

import backend.model.AstartesCategory;
import backend.model.Weapon;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpaceMarineEditDTO implements TokenizedDTO {
    private Long id;
    private String name;
    private int x;
    private Integer y;
    private Long chapterId;
    private double health;
    private String achievements;
    private AstartesCategory category;
    private Weapon weaponType;
    private TokenDTO token;
}
