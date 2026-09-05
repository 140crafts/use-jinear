package co.jinear.core.mcp;

import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpJsonSchema;
import co.jinear.core.model.mcp.McpToolException;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.service.mcp.tool.McpShapes;
import co.jinear.core.service.mcp.tool.McpTool;
import co.jinear.core.service.mcp.tool.SimpleMcpTool;

/**
 * Stand in tools for the protocol tests. Using fakes here keeps the transport tests
 * independent of any manager, so a failure points at the protocol rather than at the
 * domain underneath it.
 */
final class McpTestTools {

    private McpTestTools() {
    }

    static McpTool publicTool() {
        return SimpleMcpTool.named("public_ping")
                .title("Public ping")
                .description("Answers without any credential. Used to prove the catalog is readable before sign in.")
                .input(McpJsonSchema.noArguments())
                .readOnly()
                .handler((context, arguments) -> McpToolResult.of(McpShapes.acknowledgement("pong", "yes")))
                .build();
    }

    static McpTool readTool() {
        return SimpleMcpTool.named("read_something")
                .title("Read something")
                .description("Reads a record. Requires the task read scope.")
                .input(McpJsonSchema.noArguments())
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .handler((context, arguments) -> McpToolResult.of(McpShapes.acknowledgement("accountId", context.getAccountId())))
                .build();
    }

    static McpTool writeTool() {
        return SimpleMcpTool.named("write_something")
                .title("Write something")
                .description("Writes a record. Requires the task write scope.")
                .input(McpJsonSchema.object().requiredString("title", "What to write.").build())
                .write()
                .scopes(OauthScope.TASKS_WRITE)
                .handler((context, arguments) -> McpToolResult.of(McpShapes.acknowledgement("ok", "written")))
                .build();
    }

    static McpTool throwingTool() {
        return SimpleMcpTool.named("bad_arguments")
                .title("Bad arguments")
                .description("Always reports an argument problem. Used to prove a tool error is not a protocol error.")
                .input(McpJsonSchema.noArguments())
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .handler((context, arguments) -> {
                    throw new McpToolException("invalid_argument", "taskId must look like a ULID. Received: banana");
                })
                .build();
    }

    static McpTool explodingTool() {
        return SimpleMcpTool.named("explodes")
                .title("Explodes")
                .description("Throws an unexpected failure. Used to prove the caller still gets an actionable message.")
                .input(McpJsonSchema.noArguments())
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .handler((context, arguments) -> {
                    throw new IllegalStateException("database is on fire");
                })
                .build();
    }
}
