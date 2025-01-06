package backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import backend.exceptions.ApiException;
import backend.exceptions.DatabaseConflictException;
import backend.exceptions.DoesNotExistException;
import backend.exceptions.ForbiddenException;
import backend.exceptions.WrongPasswordException;
import backend.model.validators.Validator;

public class ControllerExecutor {
    public static ResponseEntity<?> execute(Validator validator, ControllerRunner controllerFunc) {
        try {
            if (validator.hasViolations()) {
                throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, validator.getDescription());
            }
            try {
                return controllerFunc.run();
            // } catch (AuthenticationException ex) {
                // throw new ApiException(HttpStatus.UNAUTHORIZED, ex.getMessage());
            } catch (DoesNotExistException ex) {
                throw new ApiException(HttpStatus.NOT_FOUND, ex.getMessage());
            } catch (DatabaseConflictException ex) {
                throw new ApiException(HttpStatus.CONFLICT, ex.getMessage());
            } catch (WrongPasswordException ex) {
                throw new ApiException(HttpStatus.CONFLICT, ex.getMessage());
            } catch (ForbiddenException ex) {
                throw new ApiException(HttpStatus.FORBIDDEN, ex.getMessage());
            } catch (Exception ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
        } catch (ApiException e) {
            return ResponseEntity.status(e.get().getStatus()).body(e.get());
        }
    }

    public interface ControllerRunner {
        public ResponseEntity<?> run() throws Exception;
    }
}
