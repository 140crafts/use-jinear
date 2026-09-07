package co.jinear.core.controller.mcp;

import co.jinear.core.manager.mcp.McpManager;
import co.jinear.core.model.mcp.jsonrpc.McpExchange;
import co.jinear.core.model.mcp.jsonrpc.McpJsonRpcPayload;
import co.jinear.core.model.mcp.jsonrpc.McpJsonRpcRequestBatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The MCP streamable HTTP endpoint. Its path and status codes are fixed by the MCP
 * specification, so this controller returns {@code ResponseEntity} rather than a
 * {@code BaseResponse}. It holds no logic: {@code McpManager} decides everything.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class McpController {

    private final McpManager mcpManager;

    @PostMapping(value = "/mcp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<McpJsonRpcPayload> handle(@RequestBody McpJsonRpcRequestBatch mcpJsonRpcRequestBatch) {
        McpExchange exchange = mcpManager.handle(mcpJsonRpcRequestBatch);
        return exchange.isAccepted()
                ? ResponseEntity.accepted().build()
                : ResponseEntity.ok(exchange.getBody());
    }

    @GetMapping("/mcp")
    public ResponseEntity<Void> noStream() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.ALLOW, "POST, DELETE")
                .build();
    }

    @DeleteMapping("/mcp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void endSession() {
        // The MCP session is stateless, so there is nothing to tear down.
    }
}
