package co.jinear.core.service.mcp.tool.config;

import co.jinear.core.manager.task.TaskBoardEntryManager;
import co.jinear.core.manager.task.TaskBoardListingManager;
import co.jinear.core.manager.task.TaskBoardManager;
import co.jinear.core.manager.topic.TopicListingManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpJsonSchema;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.request.task.TaskBoardEntryInitializeRequest;
import co.jinear.core.model.request.task.TaskBoardInitializeRequest;
import co.jinear.core.service.mcp.tool.McpShapes;
import co.jinear.core.service.mcp.tool.McpTool;
import co.jinear.core.service.mcp.tool.McpToolArguments;
import co.jinear.core.service.mcp.tool.SimpleMcpTool;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Boards group tasks for a sprint or a piece of work, and topics label them.
 */
@Configuration
@RequiredArgsConstructor
public class BoardMcpTools {

    private final TaskBoardManager taskBoardManager;
    private final TaskBoardListingManager taskBoardListingManager;
    private final TaskBoardEntryManager taskBoardEntryManager;
    private final TopicListingManager topicListingManager;

    @Bean
    public McpTool listTaskBoardsTool() {
        return SimpleMcpTool.named("list_task_boards")
                .title("List a team's boards")
                .description("Lists the boards belonging to a team, newest first. "
                        + "A board groups tasks for a sprint, a release or any other batch of work.")
                .input(McpJsonSchema.object()
                        .requiredString("workspaceId", "Workspace id, from list_workspaces.")
                        .requiredString("teamId", "Team id, from list_teams.")
                        .integer("page", "Zero based page number. Defaults to 0.")
                        .build())
                .output(McpShapes.pageSchema("Boards in this team.", McpShapes.boardSchema()))
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    String workspaceId = args.requiredString("workspaceId");
                    context.setWorkspaceId(workspaceId);
                    var page = taskBoardListingManager
                            .retrieveAllByTeam(workspaceId, args.requiredString("teamId"), args.page())
                            .getTaskListDetailedDtoPageDto();
                    return McpToolResult.of(McpShapes.page(page, McpShapes::board));
                })
                .build();
    }

    @Bean
    public McpTool createTaskBoardTool() {
        return SimpleMcpTool.named("create_task_board")
                .title("Create a board")
                .description("Creates an empty board in a team. Add tasks to it afterwards with add_task_to_board.")
                .input(McpJsonSchema.object()
                        .requiredString("workspaceId", "Workspace id, from list_workspaces.")
                        .requiredString("teamId", "Team id, from list_teams.")
                        .requiredString("title", "Board name, for example Sprint 14.")
                        .string("dueDate", "ISO 8601 instant the board's work is due.")
                        .build())
                .output(McpShapes.singleSchema("board", "The created board.", McpShapes.boardSchema()))
                .write()
                .scopes(OauthScope.TASKS_WRITE)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    TaskBoardInitializeRequest request = new TaskBoardInitializeRequest();
                    request.setWorkspaceId(args.requiredString("workspaceId"));
                    request.setTeamId(args.requiredString("teamId"));
                    request.setTitle(args.requiredString("title"));
                    request.setDueDate(args.optionalZonedDateTime("dueDate"));
                    context.setWorkspaceId(request.getWorkspaceId());
                    var response = taskBoardManager.initializeTaskBoard(request);
                    return McpToolResult.of(McpShapes.single("board", McpShapes.board(response.getTaskBoardDto())));
                })
                .build();
    }

    @Bean
    public McpTool addTaskToBoardTool() {
        return SimpleMcpTool.named("add_task_to_board")
                .title("Add a task to a board")
                .description("Puts an existing task onto a board. The task keeps its team, status and assignee; "
                        + "only its board membership changes.")
                .input(McpJsonSchema.object()
                        .requiredString("taskBoardId", "Board id, from list_task_boards.")
                        .requiredString("taskId", "Task id, from search_tasks or list_tasks.")
                        .build())
                .output(McpShapes.acknowledgementSchema("taskBoardId", "The board the task was added to."))
                .write()
                .idempotent()
                .scopes(OauthScope.TASKS_WRITE)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    TaskBoardEntryInitializeRequest request = new TaskBoardEntryInitializeRequest();
                    request.setTaskBoardId(args.requiredString("taskBoardId"));
                    request.setTaskId(args.requiredString("taskId"));
                    taskBoardEntryManager.initializeTaskBoardEntry(request);
                    return McpToolResult.of(McpShapes.acknowledgement("taskBoardId", request.getTaskBoardId()));
                })
                .build();
    }

    @Bean
    public McpTool listTopicsTool() {
        return SimpleMcpTool.named("list_topics")
                .title("List a team's topics")
                .description("Lists the topics a team uses to label its tasks. "
                        + "Read this to turn a label a person named into the topicId create_task takes.")
                .input(McpJsonSchema.object()
                        .requiredString("teamId", "Team id, from list_teams.")
                        .integer("page", "Zero based page number. Defaults to 0.")
                        .build())
                .output(McpShapes.pageSchema("Topics in this team.", McpJsonSchema.object()
                        .string("topicId", "Topic id, the value create_task takes as topicId.")
                        .string("teamId", "Team this topic belongs to.")
                        .string("name", "Display name.")
                        .string("tag", "Short prefix used in task references.")
                        .string("color", "Display colour.")
                        .build()))
                .readOnly()
                .scopes(OauthScope.WORKSPACE_READ)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    var page = topicListingManager
                            .retrieveTeamTopics(args.requiredString("teamId"), args.page())
                            .getTopicDtoPage();
                    return McpToolResult.of(McpShapes.page(page, McpShapes::topic));
                })
                .build();
    }
}
