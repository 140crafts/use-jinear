package co.jinear.core.model.mcp.jsonrpc;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

import java.util.ArrayList;
import java.util.List;

public class McpJsonRpcRequestBatchDeserializer extends ValueDeserializer<McpJsonRpcRequestBatch> {

    @Override
    public McpJsonRpcRequestBatch deserialize(JsonParser parser, DeserializationContext context) {
        JsonNode body = context.readTree(parser);
        if (!body.isArray()) {
            return new McpJsonRpcRequestBatch(List.of(context.readTreeAsValue(body, McpJsonRpcRequest.class)), false);
        }
        List<McpJsonRpcRequest> messages = new ArrayList<>();
        for (JsonNode message : body) {
            messages.add(context.readTreeAsValue(message, McpJsonRpcRequest.class));
        }
        return new McpJsonRpcRequestBatch(messages, true);
    }
}
