package telerik.project.movielibrary.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import telerik.project.movielibrary.models.dtos.api.ApiResponseDTO;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationFailureException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleAuthentication(
            AuthenticationFailureException e,
            HttpServletRequest request
    ) {
        return error(HttpStatus.UNAUTHORIZED, request, e.getMessage());
    }

    @ExceptionHandler(AuthorizationFailureException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleAuthorization(
            AuthorizationFailureException e,
            HttpServletRequest request
    ) {
        return error(HttpStatus.FORBIDDEN, request, e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleNotFound(
            EntityNotFoundException e,
            HttpServletRequest request
    ) {
        return error(HttpStatus.NOT_FOUND, request, e.getMessage());
    }

    @ExceptionHandler(EntityDuplicateException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleDuplicate(
            EntityDuplicateException e,
            HttpServletRequest request
    ) {
        return error(HttpStatus.CONFLICT, request, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleValidation(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponseDTO.validationError(
                        HttpStatus.UNPROCESSABLE_ENTITY.value(),
                        request.getRequestURI(),
                        "Validation failed,",
                        errors
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleUnexpected(HttpServletRequest request) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, request, "Unexpected server error.");
    }

    private ResponseEntity<ApiResponseDTO<Void>> error(
            HttpStatus status,
            HttpServletRequest request,
            String message
    ) {
        return ResponseEntity
                .status(status)
                .body(ApiResponseDTO.error(
                        status.value(), request.getRequestURI(), message
                ));
    }
}
