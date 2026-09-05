package co.jinear.core.service.mcp.tool;

import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolAnnotations;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiFunction;

public final class SimpleMcpTool implements McpTool {

    private final McpToolDefinition definition;
    private final BiFunction<McpToolContext, JsonNode, McpToolResult> handler;

    private SimpleMcpTool(McpToolDefinition definition, BiFunction<McpToolContext, JsonNode, McpToolResult> handler) {
        this.definition = definition;
        this.handler = handler;
    }

    public static Builder named(String name) {
        return new Builder(name);
    }

    @Override
    public McpToolDefinition definition() {
        return definition;
    }

    @Override
    public McpToolResult call(McpToolContext context, JsonNode arguments) {
        return handler.apply(context, arguments);
    }

    public static final class Builder {

        private final String name;
        private final Set<OauthScope> scopes = new LinkedHashSet<>();
        private String title;
        private String description;
        private ObjectNode inputSchema;
        private ObjectNode outputSchema;
        private boolean readOnly;
        private boolean destructive;
        private boolean idempotent;
        private BiFunction<McpToolContext, JsonNode, McpToolResult> handler;

        private Builder(String name) {
            this.name = name;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder input(ObjectNode inputSchema) {
            this.inputSchema = inputSchema;
            return this;
        }

        public Builder output(ObjectNode outputSchema) {
            this.outputSchema = outputSchema;
            return this;
        }

        public Builder readOnly() {
            this.readOnly = true;
            this.idempotent = true;
            return this;
        }

        public Builder write() {
            this.readOnly = false;
            this.destructive = false;
            return this;
        }

        public Builder destructive() {
            this.readOnly = false;
            this.destructive = true;
            return this;
        }

        public Builder idempotent() {
            this.idempotent = true;
            return this;
        }

        public Builder scopes(OauthScope... required) {
            for (OauthScope scope : required) {
                scopes.add(scope);
            }
            return this;
        }

        public Builder handler(BiFunction<McpToolContext, JsonNode, McpToolResult> handler) {
            this.handler = handler;
            return this;
        }

        public SimpleMcpTool build() {
            McpToolAnnotations annotations = McpToolAnnotations.builder()
                    .title(title)
                    .readOnlyHint(readOnly)
                    .destructiveHint(destructive)
                    .idempotentHint(idempotent)
                    .openWorldHint(false)
                    .build();
            McpToolDefinition definition = McpToolDefinition.builder()
                    .name(name)
                    .description(description)
                    .inputSchema(inputSchema)
                    .outputSchema(outputSchema)
                    .annotations(annotations)
                    .requiredScopes(Set.copyOf(scopes))
                    .build();
            return new SimpleMcpTool(definition, handler);
        }
    }
}
