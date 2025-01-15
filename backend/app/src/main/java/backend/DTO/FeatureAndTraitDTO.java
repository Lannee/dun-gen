package backend.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeatureAndTraitDTO {
    private String name;
    private String description;
}
