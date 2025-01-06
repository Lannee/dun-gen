package backend.model.validators;

import java.util.List;
import java.util.LinkedList;

public class Validator {
    private final List<Violation> violations;

    public Validator() {
        violations = new LinkedList<Violation>();
    }

    public void addViolation(String field, String violation) {
        violations.add(new Violation(field, violation));
    }

    public boolean hasViolations() {
        return !violations.isEmpty();
    }

    public String getDescription() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < violations.size(); i++) {
            stringBuilder.append(violations.get(i).getMessageError() + " ");
        }
        return stringBuilder.toString();
    }
}
