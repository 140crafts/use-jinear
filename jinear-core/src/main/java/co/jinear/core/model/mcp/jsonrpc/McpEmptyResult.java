package co.jinear.core.model.mcp.jsonrpc;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = McpEmptyResult.EmptyObjectSerializer.class)
public class McpEmptyResult implements McpResult {

    static class EmptyObjectSerializer extends ValueSerializer<McpEmptyResult> {

        @Override
        public void serialize(McpEmptyResult value, JsonGenerator generator, SerializationContext context) {
            generator.writeStartObject();
            generator.writeEndObject();
        }
    }
}
