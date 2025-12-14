package telerik.project.movielibrary.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import telerik.project.movielibrary.models.dtos.ApiResponseException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationFailureException.class)
    public ResponseEntity<ApiResponseException> handleAuthentication(
            AuthenticationFailureException e,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                e.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(AuthorizationFailureException.class)
    public ResponseEntity<ApiResponseException> handleAuthorization(
            AuthorizationFailureException e,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.FORBIDDEN,
                e.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseException> handleEntityNotFound(
            EntityNotFoundException e,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(EntityDuplicateException.class)
    public ResponseEntity<ApiResponseException> handleDuplicateException(
            EntityDuplicateException e,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.CONFLICT,
                e.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseException> handleValidationErrors(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ApiResponseException response = ApiResponseException.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .details(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseException> handleUnexpected(HttpServletRequest request) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected server error.",
                request.getRequestURI()
        );
    }

    private ResponseEntity<ApiResponseException> buildResponse(
            HttpStatus status,
            String message,
            String path
    ) {
        ApiResponseException response = ApiResponseException.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(response);
    }
}
