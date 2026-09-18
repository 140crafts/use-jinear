package co.jinear.core.model.mcp;

import co.jinear.core.model.mcp.schema.McpSchemaNode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * One entry of an MCP {@code tools/list} result.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "title", "description", "inputSchema", "outputSchema", "annotations"})
public class McpToolDescriptor {

    private String name;
    private String title;
    private String description;
    private McpSchemaNode inputSchema;
    private McpSchemaNode outputSchema;
    private McpToolAnnotations annotations;

    public static McpToolDescriptor of(McpToolDefinition definition) {
        McpToolDescriptor descriptor = new McpToolDescriptor();
        descriptor.setName(definition.getName());
        descriptor.setTitle(definition.title());
        descriptor.setDescription(definition.getDescription());
        descriptor.setInputSchema(definition.getInputSchema());
        descriptor.setOutputSchema(definition.getOutputSchema());
        descriptor.setAnnotations(definition.getAnnotations());
        return descriptor;
    }
}
