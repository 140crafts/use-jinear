package co.jinear.core.model.mcp.jsonrpc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * One outbound JSON-RPC message. Exactly one of {@code result} and {@code error} is set.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"jsonrpc", "id", "result", "error"})
public class McpJsonRpcResponse implements McpJsonRpcPayload {

    private static final String VERSION = "2.0";

    private String jsonrpc = VERSION;
    private JsonNode id;
    private McpResult result;
    private McpJsonRpcError error;

    public static McpJsonRpcResponse success(JsonNode id, McpResult result) {
        McpJsonRpcResponse response = new McpJsonRpcResponse();
        response.setId(echoed(id));
        response.setResult(result);
        return response;
    }

    public static McpJsonRpcResponse failure(JsonNode id, int code, String message) {
        McpJsonRpcResponse response = new McpJsonRpcResponse();
        response.setId(echoed(id));
        response.setError(new McpJsonRpcError(code, message));
        return response;
    }

    private static JsonNode echoed(JsonNode id) {
        return Objects.isNull(id) ? NullNode.getInstance() : id;
    }
}
