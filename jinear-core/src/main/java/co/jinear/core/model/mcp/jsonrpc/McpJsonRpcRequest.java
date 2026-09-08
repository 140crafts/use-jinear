package co.jinear.core.model.mcp.jsonrpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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
