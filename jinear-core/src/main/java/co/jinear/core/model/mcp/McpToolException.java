package co.jinear.core.model.mcp;

import lombok.Getter;

@Getter
public class McpToolException extends RuntimeException {

    private final String errorCode;

    public McpToolException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
