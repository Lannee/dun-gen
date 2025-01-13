package backend.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Abilities {
    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;
}
