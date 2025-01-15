package backend.DTO;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonalityDTO {
    private List<String> traits;
    private String backstory;
}
