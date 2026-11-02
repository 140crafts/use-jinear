package co.jinear.core.manager.mcp.tool;

import co.jinear.core.model.mcp.McpToolException;
import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.text.ParseException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

@RequiredArgsConstructor
public class McpToolArguments {

    private static final String ISO_INSTANT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String ISO_DATE = "yyyy-MM-dd";

    private final JsonNode node;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public static McpToolArguments of(JsonNode node, ObjectMapper objectMapper, Validator validator) {
        return new McpToolArguments(Objects.isNull(node) || node.isNull() ? null : node, objectMapper, validator);
    }

    public <T> T bind(Class<T> inputType) {
        T bound = read(inputType);
        validate(bound, inputType);
        return bound;
    }

    private <T> T read(Class<T> inputType) {
        try {
            return objectMapper.readerFor(inputType)
                    .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .readValue(Objects.isNull(node) ? objectMapper.createObjectNode() : node);
        } catch (IOException | IllegalArgumentException exception) {
            throw unwrapToolException(exception);
        }
    }

    private <T> void validate(T bound, Class<T> inputType) {
        Set<ConstraintViolation<T>> violations = validator.validate(bound);
        if (violations.isEmpty()) {
            return;
        }
        ConstraintViolation<T> first = violations.iterator().next();
        String field = first.getPropertyPath().toString();
        String hint = hintFor(inputType, field);
        throw new McpToolException("missing_argument", "mcp.error.argument.constraint", field, first.getMessage(), hint);
    }

    private String hintFor(Class<?> inputType, String fieldName) {
        for (Class<?> current = inputType; current != null; current = current.getSuperclass()) {
            try {
                McpField declaration = current.getDeclaredField(fieldName).getAnnotation(McpField.class);
                if (Objects.nonNull(declaration) && !declaration.hint().isBlank()) {
                    return " " + declaration.hint();
                }
                return "";
            } catch (NoSuchFieldException exception) {
                continue;
            }
        }
        return "";
    }

    private McpToolException unwrapToolException(Exception exception) {
        Throwable cause = exception.getCause();
        while (Objects.nonNull(cause)) {
            if (cause instanceof McpToolException toolException) {
                return toolException;
            }
            cause = cause.getCause();
        }
        return new McpToolException("invalid_argument", "mcp.error.argument.unreadable", exception.getMessage());
    }

    public String requiredString(String field) {
        String value = optionalString(field, null);
        if (Objects.isNull(value) || value.isBlank()) {
            throw new McpToolException("missing_argument", "mcp.error.argument.string-required", field);
        }
        return value;
    }

    public String optionalString(String field, String fallback) {
        JsonNode value = get(field);
        if (Objects.isNull(value) || value.isNull()) {
            return fallback;
        }
        if (!value.isTextual()) {
            String receivedType = value.getNodeType().name().toLowerCase();
            throw new McpToolException("invalid_argument", "mcp.error.argument.string-type", field, receivedType);
        }
        return value.asText();
    }

    public Integer optionalInteger(String field, Integer fallback) {
        JsonNode value = get(field);
        if (Objects.isNull(value) || value.isNull()) {
            return fallback;
        }
        if (!value.isIntegralNumber()) {
            throw new McpToolException("invalid_argument", "mcp.error.argument.integer-type", field);
        }
        return value.asInt();
    }

    public int requiredInteger(String field, String hint) {
        Integer value = optionalInteger(field, null);
        if (Objects.isNull(value)) {
            throw new McpToolException("missing_argument", "mcp.error.argument.integer-required", field, hint);
        }
        return value;
    }

    public <E extends Enum<E>> E requiredEnum(String field, Class<E> type, String hint) {
        return parseEnum(field, requiredString(field), type, hint);
    }

    public <E extends Enum<E>> E optionalEnum(String field, Class<E> type, String hint) {
        String raw = optionalString(field, null);
        return Objects.isNull(raw) ? null : parseEnum(field, raw, type, hint);
    }

    public <E extends Enum<E>> List<E> optionalEnumList(String field, Class<E> type, String hint) {
        List<E> values = new ArrayList<>();
        for (String raw : optionalStringList(field)) {
            values.add(parseEnum(field, raw, type, hint));
        }
        return values;
    }

    private <E extends Enum<E>> E parseEnum(String field, String raw, Class<E> type, String hint) {
        try {
            return Enum.valueOf(type, raw.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new McpToolException("invalid_argument", "mcp.error.argument.enum-value", field, hint, raw);
        }
    }

    public Boolean optionalBoolean(String field, Boolean fallback) {
        JsonNode value = get(field);
        if (Objects.isNull(value) || value.isNull()) {
            return fallback;
        }
        if (!value.isBoolean()) {
            throw new McpToolException("invalid_argument", "mcp.error.argument.boolean-type", field);
        }
        return value.asBoolean();
    }

    public boolean requiredBoolean(String field) {
        Boolean value = optionalBoolean(field, null);
        if (Objects.isNull(value)) {
            throw new McpToolException("missing_argument", "mcp.error.argument.boolean-required", field);
        }
        return value;
    }

    public List<String> optionalStringList(String field) {
        JsonNode value = get(field);
        List<String> values = new ArrayList<>();
        if (Objects.isNull(value) || value.isNull()) {
            return values;
        }
        if (!value.isArray()) {
            throw new McpToolException("invalid_argument", "mcp.error.argument.string-list-type", field);
        }
        value.forEach(item -> {
            if (!item.isTextual()) {
                throw new McpToolException("invalid_argument", "mcp.error.argument.string-list-item", field);
            }
            values.add(item.asText());
        });
        return values;
    }

    public Date optionalDate(String field) {
        String raw = optionalString(field, null);
        if (Objects.isNull(raw) || raw.isBlank()) {
            return null;
        }
        Date parsed = tryParse(raw, ISO_INSTANT);
        if (Objects.isNull(parsed)) {
            parsed = tryParse(raw, ISO_DATE);
        }
        if (Objects.isNull(parsed)) {
            throw new McpToolException("invalid_argument", "mcp.error.argument.date-format", field, raw);
        }
        return parsed;
    }

    public Date requiredDate(String field) {
        Date value = optionalDate(field);
        if (Objects.isNull(value)) {
            throw new McpToolException("missing_argument", "mcp.error.argument.date-required", field);
        }
        return value;
    }

    public ZonedDateTime optionalZonedDateTime(String field) {
        Date parsed = optionalDate(field);
        return Objects.isNull(parsed) ? null : ZonedDateTime.ofInstant(parsed.toInstant(), ZoneOffset.UTC);
    }

    public ZonedDateTime requiredZonedDateTime(String field) {
        return ZonedDateTime.ofInstant(requiredDate(field).toInstant(), ZoneOffset.UTC);
    }

    public boolean has(String field) {
        JsonNode value = get(field);
        return Objects.nonNull(value) && !value.isNull();
    }

    public int page() {
        Integer page = optionalInteger("page", 0);
        if (page < 0) {
            throw new McpToolException("invalid_argument", "mcp.error.argument.page");
        }
        return page;
    }

    public int pageSize(int max) {
        Integer pageSize = optionalInteger("pageSize", 20);
        if (pageSize < 1 || pageSize > max) {
            throw new McpToolException("invalid_argument", "mcp.error.argument.page-size", String.valueOf(max));
        }
        return pageSize;
    }

    private JsonNode get(String field) {
        return Objects.isNull(node) ? null : node.get(field);
    }

    private Date tryParse(String raw, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            format.setLenient(false);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            return format.parse(raw);
        } catch (ParseException exception) {
            return null;
        }
    }
}
