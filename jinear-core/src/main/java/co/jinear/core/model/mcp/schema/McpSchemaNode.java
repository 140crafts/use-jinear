package co.jinear.core.model.mcp.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * One node of a JSON Schema document, as MCP publishes it for a tool's input and output.
 * <p>
 * {@code properties} is a genuine key to value map: the keys are property names carried as
 * data, not a stand in for fields we failed to declare.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "format", "description", "properties", "required", "additionalProperties",
        "items", "enum", "minimum", "maximum"})
public class McpSchemaNode {

    public static final String TYPE_OBJECT = "object";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_INTEGER = "integer";
    public static final String TYPE_BOOLEAN = "boolean";
    public static final String TYPE_ARRAY = "array";

    public static final String FORMAT_DATE_TIME = "date-time";

    private String type;
    private String format;
    private String description;
    private Map<String, McpSchemaNode> properties;
    private List<String> required;
    private Boolean additionalProperties;
    private McpSchemaNode items;

    @JsonProperty("enum")
    private List<String> enumValues;

    private Integer minimum;
    private Integer maximum;

    public static McpSchemaNode of(String type, String description) {
        McpSchemaNode node = new McpSchemaNode();
        node.setType(type);
        node.setDescription(description);
        return node;
    }

    public static McpSchemaNode objectNode() {
        McpSchemaNode node = new McpSchemaNode();
        node.setType(TYPE_OBJECT);
        node.setProperties(new LinkedHashMap<>());
        node.setAdditionalProperties(Boolean.FALSE);
        return node;
    }

    public McpSchemaNode putProperty(String name, McpSchemaNode property) {
        properties.put(name, property);
        return this;
    }

    public McpSchemaNode copyWithDescription(String newDescription) {
        McpSchemaNode copy = new McpSchemaNode();
        copy.setType(type);
        copy.setFormat(format);
        copy.setDescription(newDescription);
        copy.setProperties(properties);
        copy.setRequired(required);
        copy.setAdditionalProperties(additionalProperties);
        copy.setItems(items);
        copy.setEnumValues(enumValues);
        copy.setMinimum(minimum);
        copy.setMaximum(maximum);
        return copy;
    }
}
