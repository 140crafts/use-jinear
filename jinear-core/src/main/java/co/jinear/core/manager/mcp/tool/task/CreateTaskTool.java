package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskInitializeManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpTaskEnvelopeView;
import co.jinear.core.model.mcp.view.McpTaskView;
import co.jinear.core.model.request.task.TaskInitializeRequest;
import co.jinear.core.model.response.task.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.jinear.core.model.mcp.input.McpCreateTaskInput;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CreateTaskTool implements McpTool {

    private final TaskInitializeManager taskInitializeManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("create_task")
                .title("Create a task")
                .description("Creates a task in a team. Only workspaceId, teamId and title are required. "
                             + "Give it a due date to put it on the calendar, and an assignee account id to hand it to someone. "
                             + "Returns the created task, including its reference such as ENG-42.")
                .input(McpSchemaGenerator.forInput(McpCreateTaskInput.class))
                .output(McpSchemaGenerator.single("task", "The created task.", McpTaskView.class))
                .write()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpCreateTaskInput input = args.bind(McpCreateTaskInput.class);
        TaskInitializeRequest request = new TaskInitializeRequest();
        request.setWorkspaceId(input.getWorkspaceId());
        request.setTeamId(input.getTeamId());
        request.setTitle(input.getTitle());
        request.setDescription(input.getDescription());
        request.setAssignedTo(input.getAssignedTo());
        request.setAssignedDate(input.getStartDate());
        request.setDueDate(input.getDueDate());
        request.setHasPreciseAssignedDate(Objects.nonNull(input.getStartDate()));
        request.setHasPreciseDueDate(Objects.nonNull(input.getDueDate()));
        request.setTopicId(input.getTopicId());
        request.setBoardId(input.getBoardId());
        context.setWorkspaceId(input.getWorkspaceId());
        TaskResponse response = taskInitializeManager.initializeTask(request);
        return McpToolResult.of(McpTaskEnvelopeView.of(mcpViewConverter.task(response.getTaskDto())));
    }
}
