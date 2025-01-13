package backend.DTO;

import lombok.Data;

@Data
public class IdDTO implements TokenizedDTO {
    private int id;
    private TokenDTO token;
}
