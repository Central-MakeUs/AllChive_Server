package allchive.server.core.error;


import allchive.server.core.dto.ErrorReason;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    private final boolean success = false;
    private String code;
    private int status;
    private String reason;

    @Builder
    private ErrorResponse(String code, int status, String reason) {
        this.code = code;
        this.status = status;
        this.reason = reason;
    }

    public static ErrorResponse from(ErrorReason errorReason) {
        return ErrorResponse.builder()
                .code(errorReason.getCode())
                .status(errorReason.getStatus())
                .reason(errorReason.getReason())
                .build();
    }
}
