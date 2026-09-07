package co.jinear.core.model.mcp;

import co.jinear.core.model.mcp.schema.McpSchemaNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Declarative builder for a tool's input schema. Output schemas are generated from the
 * payload class instead, see {@code McpSchemaGenerator}.
 */
public final class McpJsonSchema {

    private final McpSchemaNode schema;
    private final List<String> required = new ArrayList<>();

    private McpJsonSchema() {
        this.schema = McpSchemaNode.objectNode();
    }

    public static McpJsonSchema object() {
        return new McpJsonSchema();
    }

    public static McpSchemaNode noArguments() {
        McpSchemaNode node = McpSchemaNode.objectNode();
        node.setRequired(null);
        return node;
    }

    public McpJsonSchema string(String name, String description) {
        return property(name, McpSchemaNode.TYPE_STRING, description, false);
    }

    public McpJsonSchema requiredString(String name, String description) {
        return property(name, McpSchemaNode.TYPE_STRING, description, true);
    }

    public McpJsonSchema integer(String name, String description) {
        return property(name, McpSchemaNode.TYPE_INTEGER, description, false);
    }

    public McpJsonSchema requiredInteger(String name, String description) {
        return property(name, McpSchemaNode.TYPE_INTEGER, description, true);
    }

    public McpJsonSchema bool(String name, String description) {
        return property(name, McpSchemaNode.TYPE_BOOLEAN, description, false);
    }

    public McpJsonSchema requiredBool(String name, String description) {
        return property(name, McpSchemaNode.TYPE_BOOLEAN, description, true);
    }

    public McpJsonSchema enumeration(String name, String description, List<String> values, boolean isRequired) {
        McpSchemaNode node = McpSchemaNode.of(McpSchemaNode.TYPE_STRING, description);
        node.setEnumValues(List.copyOf(values));
        return put(name, node, isRequired);
    }

    public McpJsonSchema stringArray(String name, String description, boolean isRequired) {
        McpSchemaNode node = McpSchemaNode.of(McpSchemaNode.TYPE_ARRAY, description);
        node.setItems(McpSchemaNode.of(McpSchemaNode.TYPE_STRING, null));
        return put(name, node, isRequired);
    }

    public McpJsonSchema withPaging(int maxPageSize) {
        McpSchemaNode page = McpSchemaNode.of(McpSchemaNode.TYPE_INTEGER, "Zero based page number. Defaults to 0.");
        page.setMinimum(0);
        McpSchemaNode pageSize = McpSchemaNode.of(McpSchemaNode.TYPE_INTEGER,
                "Items per page, from 1 to " + maxPageSize + ". Defaults to 20.");
        pageSize.setMinimum(1);
        pageSize.setMaximum(maxPageSize);
        schema.putProperty("page", page);
        schema.putProperty("pageSize", pageSize);
        return this;
    }

    public McpJsonSchema allowAdditional() {
        schema.setAdditionalProperties(Boolean.TRUE);
        return this;
    }

    public McpSchemaNode build() {
        if (!required.isEmpty()) {
            schema.setRequired(List.copyOf(required));
        }
        return schema;
    }

    private McpJsonSchema property(String name, String type, String description, boolean isRequired) {
        return put(name, McpSchemaNode.of(type, description), isRequired);
    }

    private McpJsonSchema put(String name, McpSchemaNode node, boolean isRequired) {
        schema.putProperty(name, node);
        if (isRequired) {
            required.add(name);
        }
        return this;
    }
}
