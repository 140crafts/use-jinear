package co.jinear.core.manager.mcp;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.service.mcp.McpToolCallLogService;
import co.jinear.core.model.mcp.McpToolException;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.jsonrpc.McpContentBlock;
import co.jinear.core.model.mcp.jsonrpc.McpEmptyResult;
import co.jinear.core.model.mcp.jsonrpc.McpInitializeParams;
import co.jinear.core.model.mcp.jsonrpc.McpInitializeResult;
import co.jinear.core.model.mcp.jsonrpc.McpJsonRpcError;
import co.jinear.core.model.mcp.jsonrpc.McpJsonRpcRequest;
import co.jinear.core.model.mcp.jsonrpc.McpJsonRpcResponse;
import co.jinear.core.model.mcp.jsonrpc.McpServerCapabilities;
import co.jinear.core.model.mcp.jsonrpc.McpServerInfo;
import co.jinear.core.model.mcp.jsonrpc.McpToolCallParams;
import co.jinear.core.model.mcp.jsonrpc.McpToolCallResult;
import co.jinear.core.model.mcp.jsonrpc.McpToolsCapability;
import co.jinear.core.model.mcp.jsonrpc.McpToolsListResult;
import co.jinear.core.model.mcp.view.McpToolPayload;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class McpProtocolManager {

    public static final String PREFERRED_PROTOCOL_VERSION = "2025-11-25";
    public static final List<String> SUPPORTED_PROTOCOL_VERSIONS =
            List.of("2025-11-25", "2025-06-18", "2025-03-26");

    public static final String SERVER_NAME = "jinear";
    public static final String SERVER_TITLE = "Jinear";
    public static final String SERVER_VERSION = "1.0.0";

    private static final String METHOD_INITIALIZE = "initialize";
    private static final String METHOD_PING = "ping";
    private static final String METHOD_TOOLS_LIST = "tools/list";
    private static final String METHOD_TOOLS_CALL = "tools/call";

    private static final String INSTRUCTIONS = """
            Jinear holds a person's tasks, boards, calendar, notes and files. \
            Call list_workspaces first: nearly every other tool needs a workspaceId, and \
            task tools also need a teamId from list_teams. Before changing a task's status, \
            read list_workflow_statuses for that team, since statuses are per team and are \
            referenced by id. Search tools accept plain language. Dates are ISO 8601 in UTC.""";

    private final McpToolRegistry mcpToolRegistry;
    private final McpToolCallLogService mcpToolCallLogService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public Optional<McpJsonRpcResponse> handle(McpJsonRpcRequest request, McpToolContext context) {
        if (Objects.isNull(request.getMethod())) {
            return Optional.of(McpJsonRpcResponse.failure(request.getId(),
                    McpJsonRpcError.INVALID_REQUEST, "Missing method."));
        }
        if (request.isNotification()) {
            log.debug("[MCP] Notification received: {}", request.getMethod());
            return Optional.empty();
        }

        return Optional.of(switch (request.getMethod()) {
            case METHOD_INITIALIZE -> McpJsonRpcResponse.success(request.getId(), initialize(request.getParams()));
            case METHOD_PING -> McpJsonRpcResponse.success(request.getId(), new McpEmptyResult());
            case METHOD_TOOLS_LIST -> McpJsonRpcResponse.success(request.getId(),
                    new McpToolsListResult(mcpToolRegistry.descriptors()));
            case METHOD_TOOLS_CALL -> toolsCall(request, context);
            default -> McpJsonRpcResponse.failure(request.getId(),
                    McpJsonRpcError.METHOD_NOT_FOUND, "Unknown method: " + request.getMethod());
        });
    }

    public boolean isToolCall(McpJsonRpcRequest request) {
        return METHOD_TOOLS_CALL.equals(request.getMethod());
    }

    public String toolNameOf(McpJsonRpcRequest request) {
        return toolCallParams(request).getName();
    }

    private McpInitializeResult initialize(JsonNode params) {
        String requested = readParams(params, McpInitializeParams.class).getProtocolVersion();
        String negotiated = SUPPORTED_PROTOCOL_VERSIONS.contains(requested) ? requested : PREFERRED_PROTOCOL_VERSION;
        return new McpInitializeResult(
                negotiated,
                new McpServerCapabilities(new McpToolsCapability(false)),
                new McpServerInfo(SERVER_NAME, SERVER_TITLE, SERVER_VERSION),
                INSTRUCTIONS);
    }

    private McpJsonRpcResponse toolsCall(McpJsonRpcRequest request, McpToolContext context) {
        McpToolCallParams params = toolCallParams(request);
        String name = params.getName();
        if (Objects.isNull(name)) {
            return McpJsonRpcResponse.failure(request.getId(),
                    McpJsonRpcError.INVALID_PARAMS, "tools/call requires a tool name.");
        }
        Optional<McpTool> tool = mcpToolRegistry.find(name);
        if (tool.isEmpty()) {
            return McpJsonRpcResponse.failure(request.getId(),
                    McpJsonRpcError.INVALID_PARAMS, "Unknown tool: " + name);
        }

        McpToolArguments arguments = McpToolArguments.of(params.getArguments(), objectMapper, validator);
        long startedAt = System.currentTimeMillis();
        try {
            McpToolResult result = tool.get().call(context, arguments);
            McpToolCallResult callResult = wrap(result);
            mcpToolCallLogService.recordOutcome(context, name, result.isError(), null,
                    System.currentTimeMillis() - startedAt, serialize(callResult).length());
            return McpJsonRpcResponse.success(request.getId(), callResult);
        } catch (McpToolException toolException) {
            mcpToolCallLogService.recordOutcome(context, name, true, toolException.getErrorCode(),
                    System.currentTimeMillis() - startedAt, 0);
            return McpJsonRpcResponse.success(request.getId(), wrap(McpToolResult.error(toolException.getMessage())));
        } catch (RuntimeException exception) {
            log.error("[MCP] Tool {} failed.", name, exception);
            mcpToolCallLogService.recordFailure(context, name, exception,
                    System.currentTimeMillis() - startedAt);
            return McpJsonRpcResponse.success(request.getId(), wrap(McpToolResult.error(describe(exception))));
        }
    }

    private McpToolCallParams toolCallParams(McpJsonRpcRequest request) {
        return readParams(request.getParams(), McpToolCallParams.class);
    }

    /**
     * Reads the {@code params} member into the shape the called method defines. An absent or
     * malformed params object reads as an empty one, so a missing field is reported by the
     * method's own check rather than as a parse failure.
     */
    private <T> T readParams(JsonNode params, Class<T> type) {
        try {
            return Objects.isNull(params) || !params.isObject()
                    ? type.getDeclaredConstructor().newInstance()
                    : objectMapper.treeToValue(params, type);
        } catch (ReflectiveOperationException | JsonProcessingException exception) {
            throw new McpToolException("invalid_params", "The params object could not be read.");
        }
    }

    private McpToolCallResult wrap(McpToolResult result) {
        McpToolCallResult callResult = new McpToolCallResult();
        if (result.isError()) {
            callResult.setContent(List.of(McpContentBlock.text(result.getText())));
            callResult.setError(true);
            return callResult;
        }
        McpToolPayload structured = result.getStructuredContent();
        callResult.setContent(List.of(McpContentBlock.text(serialize(structured))));
        callResult.setStructuredContent(structured);
        callResult.setError(false);
        return callResult;
    }

    private String serialize(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException exception) {
            log.warn("[MCP] Could not serialize a tool payload.", exception);
            return String.valueOf(value);
        }
    }

    private String describe(RuntimeException exception) {
        if (exception instanceof NoAccessException) {
            return "You do not have access to that resource in this workspace.";
        }
        if (exception instanceof NotFoundException) {
            return "No such record. Check the id and try again.";
        }
        if (exception instanceof BusinessException businessException) {
            return "The request was refused: " + businessException.getMessage();
        }
        return "The request could not be completed. Try again, or narrow the arguments.";
    }
}
