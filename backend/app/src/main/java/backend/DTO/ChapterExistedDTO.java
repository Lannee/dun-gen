package backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class ChapterExistedDTO {
    private long id;
    private String name;
    private long marinesCount;
    private String world;
    private boolean editable;
    private long user_id;
}
