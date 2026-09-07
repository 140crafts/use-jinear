package co.jinear.core.model.mcp.jsonrpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * One inbound JSON-RPC message.
 * <p>
 * {@code id} stays a raw node because JSON-RPC lets a client send a string, a number or null,
 * and the response must echo back exactly what arrived. {@code params} stays a raw node
 * because its shape is chosen by {@code method}; the protocol layer hands it to the matching
 * typed reader.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class McpJsonRpcRequest {

    private String jsonrpc;
    private JsonNode id;
    private String method;
    private JsonNode params;

    /**
     * A JSON-RPC notification carries no id and expects no response.
     */
    public boolean isNotification() {
        return Objects.isNull(id) || id.isNull();
    }
}
