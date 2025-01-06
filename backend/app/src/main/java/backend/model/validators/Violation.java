package backend.model.validators;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Violation {
    private String field;
    private String messageError;
}
