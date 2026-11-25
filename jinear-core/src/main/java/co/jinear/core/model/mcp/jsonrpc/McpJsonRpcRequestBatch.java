package co.jinear.core.model.mcp.jsonrpc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonDeserialize(using = McpJsonRpcRequestBatchDeserializer.class)
public class McpJsonRpcRequestBatch {

    private final List<McpJsonRpcRequest> messages;
    private final boolean batch;
}
