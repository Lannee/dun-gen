package backend.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends Throwable {
    private final ApiError apiError;

    public ApiException(HttpStatus status, String message) {
        apiError = new ApiError(status, message);
    }

    public ApiError get() {
        return apiError;
    }
}
