package backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneratorDTO implements TokenizedDTO {
    private String prompt;
    private TokenDTO token;
}
