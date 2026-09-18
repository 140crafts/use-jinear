package co.jinear.core.service.oauth.provider;

import co.jinear.core.model.response.oauth.OauthErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

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
            case "oauth.error.disabled" -> "temporarily_unavailable";
            default -> "invalid_request";
        };
    }

    public HttpStatus statusFor(String errorCode) {
        return "invalid_client".equals(errorCode) ? HttpStatus.UNAUTHORIZED : HttpStatus.BAD_REQUEST;
    }

    public OauthErrorResponse body(String errorCode, String description) {
        return new OauthErrorResponse(errorCode, description);
    }
}
