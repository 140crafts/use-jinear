package co.jinear.core.manager.mcp.tool;

import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolAnnotations;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.schema.McpSchemaNode;

import java.util.LinkedHashSet;
import java.util.Set;

public final class McpToolDefinitionBuilder {

    private final String name;
    private final Set<OauthScope> scopes = new LinkedHashSet<>();
    private String title;
    private String description;
    private McpSchemaNode inputSchema;
    private McpSchemaNode outputSchema;
    private boolean readOnly;
    private boolean destructive;
    private boolean idempotent;

    private McpToolDefinitionBuilder(String name) {
        this.name = name;
    }

    public static McpToolDefinitionBuilder named(String name) {
        return new McpToolDefinitionBuilder(name);
    }

    public McpToolDefinitionBuilder title(String title) {
        this.title = title;
        return this;
    }

    public McpToolDefinitionBuilder description(String description) {
        this.description = description;
        return this;
    }

    public McpToolDefinitionBuilder input(McpSchemaNode inputSchema) {
        this.inputSchema = inputSchema;
        return this;
    }

    public McpToolDefinitionBuilder output(McpSchemaNode outputSchema) {
        this.outputSchema = outputSchema;
        return this;
    }

    public McpToolDefinitionBuilder readOnly() {
        this.readOnly = true;
        this.idempotent = true;
        return this;
    }

    public McpToolDefinitionBuilder write() {
        this.readOnly = false;
        this.destructive = false;
        return this;
    }

    public McpToolDefinitionBuilder destructive() {
        this.readOnly = false;
        this.destructive = true;
        return this;
    }

    public McpToolDefinitionBuilder idempotent() {
        this.idempotent = true;
        return this;
    }

    public McpToolDefinitionBuilder scopes(OauthScope... required) {
        for (OauthScope scope : required) {
            scopes.add(scope);
        }
        return this;
    }

    public McpToolDefinition build() {
        McpToolAnnotations annotations = McpToolAnnotations.builder()
                .title(title)
                .readOnlyHint(readOnly)
                .destructiveHint(destructive)
                .idempotentHint(idempotent)
                .openWorldHint(false)
                .build();
        return McpToolDefinition.builder()
                .name(name)
                .description(description)
                .inputSchema(inputSchema)
                .outputSchema(outputSchema)
                .annotations(annotations)
                .requiredScopes(Set.copyOf(scopes))
                .build();
    }
}
