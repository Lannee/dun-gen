package backend.DTO;

import java.util.Map;

import backend.model.ImportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImportDTO {
    private ImportStatus status;
    private String userName;
    private long count;
    private String time;
    private String objectName;
    private Map<String, String> errors;
}