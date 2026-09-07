package co.jinear.core.exception.mcp;

import lombok.Getter;

/**
 * Raised when a tool call carries no token, or a token missing a scope the tool declares.
 * The RFC 6750 challenge is built here and rendered by {@code McpApiAdvice}, so no
 * WWW-Authenticate string is assembled in a controller.
 */
@Getter
public class McpAuthChallengeException extends RuntimeException {

    private final String errorCode;
    private final String challenge;
    private final boolean forbidden;

    public McpAuthChallengeException(String errorCode, String description, String challenge, boolean forbidden) {
        super(description);
        this.errorCode = errorCode;
        this.challenge = challenge;
        this.forbidden = forbidden;
    }
}
