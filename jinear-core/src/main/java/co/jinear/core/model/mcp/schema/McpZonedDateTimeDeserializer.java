package co.jinear.core.model.mcp.schema;

import co.jinear.core.model.mcp.McpToolException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class McpZonedDateTimeDeserializer extends ValueDeserializer<ZonedDateTime> {

    @Override
    public ZonedDateTime deserialize(JsonParser parser, DeserializationContext context) {
        String raw = parser.getValueAsString();
        if (Objects.isNull(raw) || raw.isBlank()) {
            return null;
        }
        ZonedDateTime instant = tryInstant(raw.trim());
        if (Objects.nonNull(instant)) {
            return instant;
        }
        ZonedDateTime date = tryDate(raw.trim());
        if (Objects.nonNull(date)) {
            return date;
        }
        throw new McpToolException("invalid_argument", "mcp.error.argument.zoned-date-format", parser.currentName(), raw);
    }

    private ZonedDateTime tryInstant(String raw) {
        try {
            return ZonedDateTime.parse(raw, DateTimeFormatter.ISO_DATE_TIME).withZoneSameInstant(ZoneOffset.UTC);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    private ZonedDateTime tryDate(String raw) {
        try {
            return LocalDate.parse(raw, DateTimeFormatter.ISO_DATE).atStartOfDay(ZoneOffset.UTC);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }
}
