package co.jinear.core.model.mcp;

import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.schema.McpSchemaNode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Getter
@Builder
@ToString
public class McpToolDefinition {

    private final String name;
    private final String description;
    private final McpSchemaNode inputSchema;
    private final McpSchemaNode outputSchema;
    private final McpToolAnnotations annotations;
    private final Set<OauthScope> requiredScopes;

    public String title() {
        return annotations.getTitle();
    }
}
