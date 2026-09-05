package co.jinear.core.service.oauth.provider;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class OauthErrorMapper {

    public String errorCodeFor(String messageKey) {
        if (Objects.isNull(messageKey)) {
            return "invalid_request";
        }
        return switch (messageKey) {
            case "oauth.error.invalid-client" -> "invalid_client";
            case "oauth.error.invalid-grant" -> "invalid_grant";
            case "oauth.error.registration-disabled" -> "invalid_client_metadata";
            case "mcp.error.disabled" -> "temporarily_unavailable";
            default -> "invalid_request";
        };
    }

    public HttpStatus statusFor(String errorCode) {
        return "invalid_client".equals(errorCode) ? HttpStatus.UNAUTHORIZED : HttpStatus.BAD_REQUEST;
    }

    public Map<String, Object> body(String errorCode, String description) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", errorCode);
        if (Objects.nonNull(description)) {
            body.put("error_description", description);
        }
        return body;
    }
}
