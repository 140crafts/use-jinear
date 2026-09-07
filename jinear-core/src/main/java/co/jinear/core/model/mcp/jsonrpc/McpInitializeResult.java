package co.jinear.core.model.mcp.jsonrpc;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({"protocolVersion", "capabilities", "serverInfo", "instructions"})
public class McpInitializeResult implements McpResult {

    private String protocolVersion;
    private McpServerCapabilities capabilities;
    private McpServerInfo serverInfo;
    private String instructions;
}
