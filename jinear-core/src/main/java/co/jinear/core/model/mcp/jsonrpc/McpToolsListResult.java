package co.jinear.core.model.mcp.jsonrpc;

import co.jinear.core.model.mcp.McpToolDescriptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class McpToolsListResult implements McpResult {

    private List<McpToolDescriptor> tools;
}
