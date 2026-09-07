package co.jinear.core.model.mcp.jsonrpc;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * A batch of JSON-RPC responses, serialized as the bare array the protocol expects.
 */
@Getter
@AllArgsConstructor
public class McpJsonRpcResponseBatch implements McpJsonRpcPayload {

    @JsonValue
    private final List<McpJsonRpcResponse> responses;
}
