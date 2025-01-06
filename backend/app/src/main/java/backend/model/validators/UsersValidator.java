package backend.model.validators;

import backend.DTO.UsersDTO;

public class UsersValidator extends Validator {
    public UsersValidator validateName(UsersDTO req) {
        if (req.getName() == null || req.getName().isEmpty()) {
            this.addViolation("name", "User login is not set or empty");
        }
        return this;
    }

    public UsersValidator validatePassword(UsersDTO req) {
        if (req.getPassword() == null || req.getPassword().isEmpty()) {
            this.addViolation("password", "User password is not set or empty");
        }
        return this;
    }
}
