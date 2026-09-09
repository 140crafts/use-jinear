package co.jinear.core.model.mcp.schema;

import co.jinear.core.model.mcp.McpToolException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class McpZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

    @Override
    public ZonedDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
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
        throw new McpToolException("invalid_argument", parser.currentName()
                + " must be an ISO 8601 date or instant, for example 2026-08-29 or 2026-08-29T14:00:00Z. Received: " + raw);
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
