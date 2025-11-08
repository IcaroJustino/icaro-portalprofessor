package portaldoprofessor.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ErrorMessage {

    private String path;
    private String method;
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private List<FieldValidationError> fieldErrors;

    public ErrorMessage(HttpServletRequest request, HttpStatus status, String message) {
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorMessage(HttpServletRequest request, HttpStatus status, String message, BindingResult result) {
        this(request, status, message);
        this.fieldErrors = result.getFieldErrors().stream()
                .map(FieldValidationError::new)
                .collect(Collectors.toList());
    }

    @Data
    public static class FieldValidationError {
        private final String field;
        private final String message;

        public FieldValidationError(FieldError fieldError) {
            this.field = fieldError.getField();
            this.message = fieldError.getDefaultMessage();
        }
    }
}
