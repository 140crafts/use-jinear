package co.jinear.core.model.mcp;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * A small builder for the JSON Schema documents that describe tool inputs and outputs.
 * <p>
 * Written by hand rather than generated from a Java type, because the description on
 * each property is what the model reads to decide how to fill it in, and generated
 * schemas do not carry that. Defaults to draft 2020-12, which is what the specification
 * assumes when no $schema is present.
 */
public final class McpJsonSchema {

    private static final JsonNodeFactory FACTORY = JsonNodeFactory.instance;

    private final ObjectNode schema;
    private final ObjectNode properties;
    private final ArrayNode required;

    private McpJsonSchema() {
        this.schema = FACTORY.objectNode();
        this.schema.put("type", "object");
        this.properties = this.schema.putObject("properties");
        this.required = this.schema.putArray("required");
        this.schema.put("additionalProperties", false);
    }

    public static McpJsonSchema object() {
        return new McpJsonSchema();
    }

    /** The recommended shape for a tool that takes nothing at all. */
    public static ObjectNode noArguments() {
        ObjectNode node = FACTORY.objectNode();
        node.put("type", "object");
        node.putObject("properties");
        node.put("additionalProperties", false);
        return node;
    }

    public McpJsonSchema string(String name, String description) {
        return property(name, "string", description, false);
    }

    public McpJsonSchema requiredString(String name, String description) {
        return property(name, "string", description, true);
    }

    public McpJsonSchema integer(String name, String description) {
        return property(name, "integer", description, false);
    }

    public McpJsonSchema requiredInteger(String name, String description) {
        return property(name, "integer", description, true);
    }

    public McpJsonSchema bool(String name, String description) {
        return property(name, "boolean", description, false);
    }

    public McpJsonSchema requiredBool(String name, String description) {
        return property(name, "boolean", description, true);
    }

    private McpJsonSchema property(String name, String type, String description, boolean isRequired) {
        ObjectNode node = properties.putObject(name);
        node.put("type", type);
        node.put("description", description);
        if (isRequired) {
            required.add(name);
        }
        return this;
    }

    public McpJsonSchema enumeration(String name, String description, List<String> values, boolean isRequired) {
        ObjectNode node = properties.putObject(name);
        node.put("type", "string");
        node.put("description", description);
        ArrayNode allowed = node.putArray("enum");
        values.forEach(allowed::add);
        if (isRequired) {
            required.add(name);
        }
        return this;
    }

    public McpJsonSchema stringArray(String name, String description, boolean isRequired) {
        ObjectNode node = properties.putObject(name);
        node.put("type", "array");
        node.put("description", description);
        node.putObject("items").put("type", "string");
        if (isRequired) {
            required.add(name);
        }
        return this;
    }

    public McpJsonSchema objectArray(String name, String description, ObjectNode itemSchema, boolean isRequired) {
        ObjectNode node = properties.putObject(name);
        node.put("type", "array");
        node.put("description", description);
        node.set("items", itemSchema);
        if (isRequired) {
            required.add(name);
        }
        return this;
    }

    public McpJsonSchema nested(String name, String description, ObjectNode nestedSchema, boolean isRequired) {
        ObjectNode node = nestedSchema.deepCopy();
        node.put("description", description);
        properties.set(name, node);
        if (isRequired) {
            required.add(name);
        }
        return this;
    }

    /** Adds a paging window with the bounds the server actually enforces. */
    public McpJsonSchema withPaging(int maxPageSize) {
        ObjectNode page = properties.putObject("page");
        page.put("type", "integer");
        page.put("description", "Zero based page number. Defaults to 0.");
        page.put("minimum", 0);
        ObjectNode pageSize = properties.putObject("pageSize");
        pageSize.put("type", "integer");
        pageSize.put("description", "Items per page, from 1 to " + maxPageSize + ". Defaults to 20.");
        pageSize.put("minimum", 1);
        pageSize.put("maximum", maxPageSize);
        return this;
    }

    public McpJsonSchema constrain(String name, String key, Object value) {
        ObjectNode node = (ObjectNode) properties.get(name);
        if (value instanceof Integer intValue) {
            node.put(key, intValue);
        } else if (value instanceof String stringValue) {
            node.put(key, stringValue);
        } else if (value instanceof Boolean booleanValue) {
            node.put(key, booleanValue);
        }
        return this;
    }

    /** Loosens the schema so unknown keys are ignored instead of rejected. */
    public McpJsonSchema allowAdditional() {
        schema.put("additionalProperties", true);
        return this;
    }

    public ObjectNode build() {
        if (required.isEmpty()) {
            schema.remove("required");
        }
        return schema;
    }
}
