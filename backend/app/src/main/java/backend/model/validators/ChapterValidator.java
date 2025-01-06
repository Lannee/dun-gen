package backend.model.validators;

import backend.DTO.ChapterDTO;

public class ChapterValidator extends Validator {
    public ChapterValidator validateChapter(ChapterDTO req) {
        if (req.getName() == "") {
            this.addViolation("name", "Name cannot be empty line.");
        }
        if (req.getMarinesCount() < 0) {
            this.addViolation("marines count", "Marines count cannot be less than zero");
        }
        if (req.getMarinesCount() > 1000) {
            this.addViolation("marines count", "Marines count cannot be greater than 1000");
        }
        return this;
    }
}
