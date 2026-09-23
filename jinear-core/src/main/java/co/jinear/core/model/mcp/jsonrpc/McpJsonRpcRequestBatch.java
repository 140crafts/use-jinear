package co.jinear.core.model.mcp.jsonrpc;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonDeserialize(using = McpJsonRpcRequestBatchDeserializer.class)
public class McpJsonRpcRequestBatch {

    private final List<McpJsonRpcRequest> messages;
    private final boolean batch;
}
