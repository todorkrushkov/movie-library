package telerik.project.movielibrary.models.dtos.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ApiResponseDTO<T> {

    private boolean success;
    private int status;
    private String path;
    private String message;
    private T data;
    private Map<String, String> errors;
    private LocalDateTime timestamp;

    public static <T> ApiResponseDTO<T> success(
            int status,
            String message,
            T data
    ) {
        return new ApiResponseDTO<>(
                true,
                status,
                null,
                message,
                data,
                null,
                LocalDateTime.now()
        );
    }

    public static ApiResponseDTO<Void> error(
            int status,
            String path,
            String message
    ) {
        return new ApiResponseDTO<>(
                true,
                status,
                path,
                message,
                null,
                null,
                LocalDateTime.now()
        );
    }

    public static ApiResponseDTO<Void> validationError(
            int status,
            String path,
            String message,
            Map<String, String> errors
    ) {
        return new ApiResponseDTO<>(
                true,
                status,
                path,
                message,
                null,
                errors,
                LocalDateTime.now()
        );
    }
}
