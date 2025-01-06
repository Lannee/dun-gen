package backend.DTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistoryCreatedDTO {
    private String userName;
    private LocalDateTime time;
}
