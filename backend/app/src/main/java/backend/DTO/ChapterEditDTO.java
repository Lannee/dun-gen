package backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChapterEditDTO implements TokenizedDTO {
    private long id;
    private String name;
    private long marinesCount;
    private String world;
    private TokenDTO token;
}
