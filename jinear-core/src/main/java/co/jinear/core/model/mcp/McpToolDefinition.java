package co.jinear.core.model.mcp;

import co.jinear.core.model.enumtype.mcp.McpScope;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Getter
@Builder
@ToString
public class McpToolDefinition {

    /** Matches ^[a-zA-Z0-9_.-]{1,64}$. Anything longer is rejected at review. */
    private final String name;
    private final String description;
    private final ObjectNode inputSchema;
    private final ObjectNode outputSchema;
    private final McpToolAnnotations annotations;
    /** Every scope the caller must already hold. An empty set means the tool is public. */
    private final Set<McpScope> requiredScopes;

    public String title() {
        return annotations.getTitle();
    }
}
