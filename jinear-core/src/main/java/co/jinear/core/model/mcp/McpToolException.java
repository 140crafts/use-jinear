package co.jinear.core.model.mcp;

import lombok.Getter;

/**
 * Thrown by a tool when its arguments are usable JSON but wrong for the operation.
 * The message is handed to the model verbatim, so it must be specific enough to retry
 * against.
 */
@Getter
public class McpToolException extends RuntimeException {

    private final String errorCode;

    public McpToolException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
