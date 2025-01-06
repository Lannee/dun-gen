package backend.DTO;

import lombok.Data;

@Data
public class ChapterDTO implements TokenizedDTO {
    private String name;
    private long marinesCount;
    private String world;
    private TokenDTO token;
}
