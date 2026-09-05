package co.jinear.core.service.mcp;

import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolException;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.service.mcp.tool.McpTool;
import co.jinear.core.service.mcp.tool.McpToolRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * JSON-RPC dispatch for the MCP endpoint.
 * <p>
 * Written directly against the wire format rather than through an SDK. The server needs
 * to refuse an unauthenticated tool call at the HTTP layer, which means the decision has
 * to be visible outside the JSON-RPC handler, and it needs to run on the Spring Boot
 * version this module already targets.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpProtocolService {

    public static final String PREFERRED_PROTOCOL_VERSION = "2025-11-25";
    /** Older revisions we still answer on, newest first. */
    public static final List<String> SUPPORTED_PROTOCOL_VERSIONS =
            List.of("2025-11-25", "2025-06-18", "2025-03-26");

    public static final String SERVER_NAME = "jinear";
    public static final String SERVER_TITLE = "Jinear";
    public static final String SERVER_VERSION = "1.0.0";

    public static final int ERROR_INVALID_REQUEST = -32600;
    public static final int ERROR_METHOD_NOT_FOUND = -32601;
    public static final int ERROR_INVALID_PARAMS = -32602;
    public static final int ERROR_INTERNAL = -32603;

    private static final JsonNodeFactory FACTORY = JsonNodeFactory.instance;

    /**
     * Cross-tool guidance, capped at 512 characters because that is the limit one of the
     * directories applies to this field.
     */
    private static final String INSTRUCTIONS = """
            Jinear holds a person's tasks, boards, calendar, notes and files. \
            Call list_workspaces first: nearly every other tool needs a workspaceId, and \
            task tools also need a teamId from list_teams. Before changing a task's status, \
            read list_workflow_statuses for that team, since statuses are per team and are \
            referenced by id. Search tools accept plain language. Dates are ISO 8601 in UTC.""";

    private final McpToolRegistry mcpToolRegistry;
    private final McpToolCallLogService mcpToolCallLogService;
    private final ObjectMapper objectMapper;

    /**
     * @return the response object, or empty when the message was a notification and the
     * specification says to answer nothing.
     */
    public Optional<ObjectNode> handle(JsonNode message, McpToolContext context) {
        if (!message.isObject()) {
            return Optional.of(error(null, ERROR_INVALID_REQUEST, "A JSON-RPC request must be an object."));
        }
        JsonNode id = message.get("id");
        String method = message.path("method").asText(null);
        if (Objects.isNull(method)) {
            return Optional.of(error(id, ERROR_INVALID_REQUEST, "Missing method."));
        }

        boolean isNotification = Objects.isNull(id) || id.isNull();
        if (isNotification) {
            log.debug("[MCP] Notification received: {}", method);
            return Optional.empty();
        }

        return Optional.of(switch (method) {
            case "initialize" -> success(id, initialize(message.path("params")));
            case "ping" -> success(id, FACTORY.objectNode());
            case "tools/list" -> success(id, toolsList());
            case "tools/call" -> toolsCall(id, message.path("params"), context);
            default -> error(id, ERROR_METHOD_NOT_FOUND, "Unknown method: " + method);
        });
    }

    public boolean isToolCall(JsonNode message) {
        return message.isObject() && "tools/call".equals(message.path("method").asText(null));
    }

    public String toolNameOf(JsonNode message) {
        return message.path("params").path("name").asText(null);
    }

    /**
     * Answers with the client's protocol version when we speak it, and with our preferred
     * one otherwise. A client that cannot live with the answer disconnects, which is the
     * negotiation the specification describes.
     */
    private ObjectNode initialize(JsonNode params) {
        String requested = params.path("protocolVersion").asText(null);
        String negotiated = SUPPORTED_PROTOCOL_VERSIONS.contains(requested) ? requested : PREFERRED_PROTOCOL_VERSION;

        ObjectNode result = FACTORY.objectNode();
        result.put("protocolVersion", negotiated);
        ObjectNode capabilities = result.putObject("capabilities");
        // The tool list is fixed at build time, so there is nothing to notify about.
        capabilities.putObject("tools").put("listChanged", false);
        ObjectNode serverInfo = result.putObject("serverInfo");
        serverInfo.put("name", SERVER_NAME);
        serverInfo.put("title", SERVER_TITLE);
        serverInfo.put("version", SERVER_VERSION);
        result.put("instructions", INSTRUCTIONS);
        return result;
    }

    private ObjectNode toolsList() {
        ObjectNode result = FACTORY.objectNode();
        result.set("tools", mcpToolRegistry.toolsArray());
        return result;
    }

    private ObjectNode toolsCall(JsonNode id, JsonNode params, McpToolContext context) {
        String name = params.path("name").asText(null);
        if (Objects.isNull(name)) {
            return error(id, ERROR_INVALID_PARAMS, "tools/call requires a tool name.");
        }
        Optional<McpTool> tool = mcpToolRegistry.find(name);
        if (tool.isEmpty()) {
            return error(id, ERROR_INVALID_PARAMS, "Unknown tool: " + name);
        }

        JsonNode arguments = params.path("arguments");
        long startedAt = System.currentTimeMillis();
        try {
            McpToolResult result = tool.get().call(context, arguments.isMissingNode() ? FACTORY.objectNode() : arguments);
            ObjectNode wrapped = wrap(result);
            mcpToolCallLogService.recordOutcome(context, name, result.isError(), null,
                    System.currentTimeMillis() - startedAt, wrapped.toString().length());
            return success(id, wrapped);
        } catch (McpToolException toolException) {
            // A usable request with unusable arguments. Reported as a tool error so the
            // model can correct it, not as a protocol error it cannot act on.
            mcpToolCallLogService.recordOutcome(context, name, true, toolException.getErrorCode(),
                    System.currentTimeMillis() - startedAt, 0);
            return success(id, wrap(McpToolResult.error(toolException.getMessage())));
        } catch (RuntimeException exception) {
            log.error("[MCP] Tool {} failed.", name, exception);
            mcpToolCallLogService.recordFailure(context, name, exception,
                    System.currentTimeMillis() - startedAt);
            return success(id, wrap(McpToolResult.error(describe(exception))));
        }
    }

    /**
     * Structured results are also serialized into a text block. The specification asks
     * for both so that clients which predate structuredContent still see the data.
     */
    private ObjectNode wrap(McpToolResult result) {
        ObjectNode node = FACTORY.objectNode();
        var content = node.putArray("content");
        if (result.isError()) {
            content.addObject().put("type", "text").put("text", result.getText());
            node.put("isError", true);
            return node;
        }
        JsonNode structured = result.getStructuredContent();
        content.addObject().put("type", "text").put("text", serialize(structured));
        node.set("structuredContent", structured);
        node.put("isError", false);
        return node;
    }

    private String serialize(JsonNode node) {
        try {
            return objectMapper.writeValueAsString(node);
        } catch (Exception exception) {
            return String.valueOf(node);
        }
    }

    /**
     * Turns an internal failure into something the model can act on. The message key of a
     * BusinessException is more useful than "Internal Server Error", and a generic error
     * body is one of the documented reasons a connector fails review.
     */
    private String describe(RuntimeException exception) {
        if (exception instanceof co.jinear.core.exception.NoAccessException) {
            return "You do not have access to that resource in this workspace.";
        }
        if (exception instanceof co.jinear.core.exception.NotFoundException) {
            return "No such record. Check the id and try again.";
        }
        if (exception instanceof co.jinear.core.exception.BusinessException businessException) {
            return "The request was refused: " + businessException.getMessage();
        }
        return "The request could not be completed. Try again, or narrow the arguments.";
    }

    public ObjectNode success(JsonNode id, ObjectNode result) {
        ObjectNode response = FACTORY.objectNode();
        response.put("jsonrpc", "2.0");
        response.set("id", Objects.isNull(id) ? FACTORY.nullNode() : id);
        response.set("result", result);
        return response;
    }

    public ObjectNode error(JsonNode id, int code, String message) {
        ObjectNode response = FACTORY.objectNode();
        response.put("jsonrpc", "2.0");
        response.set("id", Objects.isNull(id) ? FACTORY.nullNode() : id);
        ObjectNode error = response.putObject("error");
        error.put("code", code);
        error.put("message", message);
        return response;
    }
}
