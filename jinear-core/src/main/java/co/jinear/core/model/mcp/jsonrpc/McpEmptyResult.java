package co.jinear.core.model.mcp.jsonrpc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;

/**
 * The empty result MCP's {@code ping} returns. It carries no fields, so it declares how to
 * write itself rather than relying on bean introspection.
 */
@JsonSerialize(using = McpEmptyResult.EmptyObjectSerializer.class)
public class McpEmptyResult implements McpResult {

    static class EmptyObjectSerializer extends JsonSerializer<McpEmptyResult> {

        @Override
        public void serialize(McpEmptyResult value, JsonGenerator generator, SerializerProvider provider)
                throws IOException {
            generator.writeStartObject();
            generator.writeEndObject();
        }
    }
}
