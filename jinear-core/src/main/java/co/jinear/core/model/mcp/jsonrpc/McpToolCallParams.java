package co.jinear.core.model.mcp.jsonrpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

/**
 * The parameters of an MCP {@code tools/call}. {@code arguments} stays a raw tree because its
 * shape is the called tool's own input schema; {@code McpToolArguments} reads it from there.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class McpToolCallParams {

    private String name;
    private JsonNode arguments;
}
