package co.jinear.core.model.enumtype.mcp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum McpToolCallStatus {

    OK(0),
    TOOL_ERROR(1),
    PROTOCOL_ERROR(2),
    UNAUTHORIZED(3),
    FORBIDDEN(4),
    RATE_LIMITED(5),
    SERVER_ERROR(6);

    private final int value;
}
