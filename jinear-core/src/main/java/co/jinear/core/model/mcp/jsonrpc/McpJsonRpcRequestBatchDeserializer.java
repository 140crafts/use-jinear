package co.jinear.core.model.mcp.jsonrpc;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads either one JSON-RPC message or an array of them into {@link McpJsonRpcRequestBatch},
 * so the controller can take a typed body for both shapes.
 */
public class McpJsonRpcRequestBatchDeserializer extends JsonDeserializer<McpJsonRpcRequestBatch> {

    @Override
    public McpJsonRpcRequestBatch deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        ObjectMapper objectMapper = (ObjectMapper) parser.getCodec();
        JsonNode body = objectMapper.readTree(parser);
        if (!body.isArray()) {
            return new McpJsonRpcRequestBatch(List.of(objectMapper.treeToValue(body, McpJsonRpcRequest.class)), false);
        }
        List<McpJsonRpcRequest> messages = new ArrayList<>();
        for (JsonNode message : body) {
            messages.add(objectMapper.treeToValue(message, McpJsonRpcRequest.class));
        }
        return new McpJsonRpcRequestBatch(messages, true);
    }
}
