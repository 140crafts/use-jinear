package co.jinear.core.model.mcp.jsonrpc;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * One inbound MCP HTTP body. JSON-RPC allows a single message or an array of them, and the
 * response must keep the shape it arrived in, so {@code batch} records which one was sent.
 */
@Getter
@AllArgsConstructor
@JsonDeserialize(using = McpJsonRpcRequestBatchDeserializer.class)
public class McpJsonRpcRequestBatch {

    private final List<McpJsonRpcRequest> messages;
    private final boolean batch;
}
