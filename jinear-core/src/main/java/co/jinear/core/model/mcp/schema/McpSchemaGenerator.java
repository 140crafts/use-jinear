package co.jinear.core.model.mcp.schema;

import co.jinear.core.exception.BusinessException;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Turns an MCP payload class into the JSON Schema an MCP client reads for that tool's output.
 * <p>
 * The payload class is the single source of truth. A field is published only when it carries
 * {@link McpField}, and its description comes from that annotation, so a field cannot be
 * emitted without also being described.
 */
@UtilityClass
public class McpSchemaGenerator {

    private static final String ITEMS = "items";

    public static McpSchemaNode forType(Class<?> payloadType) {
        McpSchemaNode schema = McpSchemaNode.objectNode();
        for (Field field : publishedFields(payloadType)) {
            schema.putProperty(field.getName(), scalar(field));
        }
        return schema;
    }

    public static McpSchemaNode page(Class<?> itemType, String itemDescription) {
        McpSchemaNode schema = McpSchemaNode.objectNode();
        schema.putProperty(ITEMS, arrayOf(itemType, itemDescription));
        schema.putProperty("page", McpSchemaNode.of(McpSchemaNode.TYPE_INTEGER, "Zero based page number of this result."));
        schema.putProperty("pageSize", McpSchemaNode.of(McpSchemaNode.TYPE_INTEGER, "Items per page."));
        schema.putProperty("totalElements", McpSchemaNode.of(McpSchemaNode.TYPE_INTEGER, "Total matching items across all pages."));
        schema.putProperty("totalPages", McpSchemaNode.of(McpSchemaNode.TYPE_INTEGER, "Total number of pages."));
        schema.putProperty("hasNext", McpSchemaNode.of(McpSchemaNode.TYPE_BOOLEAN, "True when another page is available."));
        schema.setRequired(List.of(ITEMS));
        return schema;
    }

    public static McpSchemaNode list(Class<?> itemType, String itemDescription) {
        McpSchemaNode schema = McpSchemaNode.objectNode();
        schema.putProperty(ITEMS, arrayOf(itemType, itemDescription));
        schema.putProperty("count", McpSchemaNode.of(McpSchemaNode.TYPE_INTEGER, "Number of items returned."));
        schema.setRequired(List.of(ITEMS));
        return schema;
    }

    public static McpSchemaNode single(String key, String description, Class<?> valueType) {
        McpSchemaNode schema = McpSchemaNode.objectNode();
        schema.putProperty(key, forType(valueType).copyWithDescription(description));
        schema.setRequired(List.of(key));
        return schema;
    }

    /**
     * An object whose single required field is an array of payloads, as the compatibility
     * search tool returns.
     */
    public static McpSchemaNode arrayField(String fieldName, Class<?> itemType, String itemDescription) {
        McpSchemaNode schema = McpSchemaNode.objectNode();
        schema.putProperty(fieldName, arrayOf(itemType, itemDescription));
        schema.setRequired(List.of(fieldName));
        return schema;
    }

    public static McpSchemaNode acknowledgement(String field, String description) {
        McpSchemaNode schema = McpSchemaNode.objectNode();
        schema.putProperty(field, McpSchemaNode.of(McpSchemaNode.TYPE_STRING, description));
        schema.putProperty("ok", McpSchemaNode.of(McpSchemaNode.TYPE_BOOLEAN, "True when the operation completed."));
        return schema;
    }

    private static McpSchemaNode arrayOf(Class<?> itemType, String description) {
        McpSchemaNode array = McpSchemaNode.of(McpSchemaNode.TYPE_ARRAY, description);
        array.setItems(forType(itemType));
        return array;
    }

    private static McpSchemaNode scalar(Field field) {
        return McpSchemaNode.of(jsonTypeOf(field), field.getAnnotation(McpField.class).value());
    }

    private static String jsonTypeOf(Field field) {
        Class<?> type = field.getType();
        if (String.class.equals(type)) {
            return McpSchemaNode.TYPE_STRING;
        }
        if (Integer.class.equals(type) || Long.class.equals(type)
                || int.class.equals(type) || long.class.equals(type)) {
            return McpSchemaNode.TYPE_INTEGER;
        }
        if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            return McpSchemaNode.TYPE_BOOLEAN;
        }
        throw new BusinessException("mcp.error.unsupported-payload-field-type");
    }

    /**
     * Superclass fields first, so a detail view that extends a base view publishes the base
     * fields in the order the base declares them.
     */
    private static List<Field> publishedFields(Class<?> payloadType) {
        Deque<Class<?>> hierarchy = new ArrayDeque<>();
        for (Class<?> current = payloadType; current != null && !Object.class.equals(current); current = current.getSuperclass()) {
            hierarchy.addFirst(current);
        }
        List<Field> fields = new ArrayList<>();
        for (Class<?> current : hierarchy) {
            for (Field field : current.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(McpField.class)) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }
}
