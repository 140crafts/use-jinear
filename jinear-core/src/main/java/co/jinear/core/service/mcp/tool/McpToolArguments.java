package co.jinear.core.service.mcp.tool;

import co.jinear.core.model.mcp.McpToolException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;

import java.text.ParseException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Typed access to a tool's arguments, with error text a model can act on.
 * <p>
 * Every failure names the field, says what was expected, and says what was received.
 * A model that gets "Invalid request" has nothing to retry with; one that gets
 * "taskId is required" fixes the call on the next turn.
 */
@RequiredArgsConstructor
public class McpToolArguments {

    private static final String ISO_INSTANT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String ISO_DATE = "yyyy-MM-dd";

    private final JsonNode node;

    public static McpToolArguments of(JsonNode node) {
        return new McpToolArguments(Objects.isNull(node) || node.isNull() ? null : node);
    }

    public String requiredString(String field) {
        String value = optionalString(field, null);
        if (Objects.isNull(value) || value.isBlank()) {
            throw new McpToolException("missing_argument", field + " is required and must be a non empty string.");
        }
        return value;
    }

    public String optionalString(String field, String fallback) {
        JsonNode value = get(field);
        if (Objects.isNull(value) || value.isNull()) {
            return fallback;
        }
        if (!value.isTextual()) {
            throw new McpToolException("invalid_argument",
                    field + " must be a string, received " + value.getNodeType().name().toLowerCase() + ".");
        }
        return value.asText();
    }

    public Integer optionalInteger(String field, Integer fallback) {
        JsonNode value = get(field);
        if (Objects.isNull(value) || value.isNull()) {
            return fallback;
        }
        if (!value.isIntegralNumber()) {
            throw new McpToolException("invalid_argument", field + " must be a whole number.");
        }
        return value.asInt();
    }

    public Boolean optionalBoolean(String field, Boolean fallback) {
        JsonNode value = get(field);
        if (Objects.isNull(value) || value.isNull()) {
            return fallback;
        }
        if (!value.isBoolean()) {
            throw new McpToolException("invalid_argument", field + " must be true or false.");
        }
        return value.asBoolean();
    }

    public boolean requiredBoolean(String field) {
        Boolean value = optionalBoolean(field, null);
        if (Objects.isNull(value)) {
            throw new McpToolException("missing_argument", field + " is required and must be true or false.");
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
            throw new McpToolException("invalid_argument", field + " must be an array of strings.");
        }
        value.forEach(item -> {
            if (!item.isTextual()) {
                throw new McpToolException("invalid_argument", field + " must contain only strings.");
            }
            values.add(item.asText());
        });
        return values;
    }

    /** Accepts either a full ISO instant or a bare date, both interpreted as UTC. */
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
            throw new McpToolException("invalid_argument",
                    field + " must be an ISO 8601 date, either 2026-08-29 or 2026-08-29T14:00:00Z. Received: " + raw);
        }
        return parsed;
    }

    public Date requiredDate(String field) {
        Date value = optionalDate(field);
        if (Objects.isNull(value)) {
            throw new McpToolException("missing_argument",
                    field + " is required and must be an ISO 8601 date, for example 2026-08-29T14:00:00Z.");
        }
        return value;
    }

    /** Managers take ZonedDateTime, so parsing lands there rather than on java.util.Date. */
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
            throw new McpToolException("invalid_argument", "page must be zero or greater.");
        }
        return page;
    }

    public int pageSize(int max) {
        Integer pageSize = optionalInteger("pageSize", 20);
        if (pageSize < 1 || pageSize > max) {
            throw new McpToolException("invalid_argument", "pageSize must be between 1 and " + max + ".");
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
