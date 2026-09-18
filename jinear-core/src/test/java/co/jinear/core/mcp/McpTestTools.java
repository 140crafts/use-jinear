package co.jinear.core.mcp;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.McpToolException;
import co.jinear.core.model.mcp.McpToolResult;

final class McpTestTools {

    private McpTestTools() {
    }

    static McpTool publicTool() {
        return new McpStubTool(
                McpToolDefinitionBuilder
                        .named("public_ping")
                        .title("Public ping")
                        .description("Answers without any credential. Used to prove the catalog is readable before sign in.")
                        .input(McpSchemaGenerator.forInput(McpStubInputs.NoArguments.class))
                        .readOnly()
                        .build(),
                (context, args) -> McpToolResult.of(McpTestPayload.result("pong")));
    }

    static McpTool readTool() {
        return new McpStubTool(
                McpToolDefinitionBuilder
                        .named("read_something")
                        .title("Read something")
                        .description("Reads a record. Requires the task read scope.")
                        .input(McpSchemaGenerator.forInput(McpStubInputs.NoArguments.class))
                        .readOnly()
                        .scopes(OauthScope.TASKS_READ)
                        .build(),
                (context, args) -> McpToolResult.of(McpTestPayload.forAccount(context.getAccountId())));
    }

    static McpTool writeTool() {
        return new McpStubTool(
                McpToolDefinitionBuilder
                        .named("write_something")
                        .title("Write something")
                        .description("Writes a record. Requires the task write scope.")
                        .input(McpSchemaGenerator.forInput(McpStubInputs.TitleOnly.class))
                        .write()
                        .scopes(OauthScope.TASKS_WRITE)
                        .build(),
                (context, args) -> McpToolResult.of(McpTestPayload.result("written")));
    }

    static McpTool throwingTool() {
        return new McpStubTool(
                McpToolDefinitionBuilder
                        .named("bad_arguments")
                        .title("Bad arguments")
                        .description("Always reports an argument problem. Used to prove a tool error is not a protocol error.")
                        .input(McpSchemaGenerator.forInput(McpStubInputs.NoArguments.class))
                        .readOnly()
                        .scopes(OauthScope.TASKS_READ)
                        .build(),
                (context, args) -> {
                    throw new McpToolException("invalid_argument", "taskId must look like a ULID. Received: banana");
                });
    }

    static McpTool explodingTool() {
        return new McpStubTool(
                McpToolDefinitionBuilder
                        .named("explodes")
                        .title("Explodes")
                        .description("Throws an unexpected failure. Used to prove the caller still gets an actionable message.")
                        .input(McpSchemaGenerator.forInput(McpStubInputs.NoArguments.class))
                        .readOnly()
                        .scopes(OauthScope.TASKS_READ)
                        .build(),
                (context, args) -> {
                    throw new IllegalStateException("database is on fire");
                });
    }
}
