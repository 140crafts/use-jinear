package co.jinear.core.model.enumtype.mcp;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Outcome of a single tools/call, recorded in mcp_tool_call_log.
 * <p>
 * TOOL_ERROR is a business rule failure returned to the model as isError: true so
 * it can self correct. PROTOCOL_ERROR is a JSON-RPC level failure (unknown tool,
 * malformed arguments) that the model is unlikely to recover from.
 */
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
