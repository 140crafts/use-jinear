package co.jinear.core.model.mcp;

import lombok.Getter;

@Getter
public class McpToolException extends RuntimeException {

    private final String errorCode;
    private final Object[] arguments;

    public McpToolException(String errorCode, String message, Object... arguments) {
        super(message);
        this.errorCode = errorCode;
        this.arguments = arguments;
    }
}
