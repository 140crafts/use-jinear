package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskUpdateManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpUpdateTaskInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpTaskAcknowledgementView;
import co.jinear.core.model.request.task.TaskAssigneeUpdateRequest;
import co.jinear.core.model.request.task.TaskDateUpdateRequest;
import co.jinear.core.model.request.task.TaskUpdateDescriptionRequest;
import co.jinear.core.model.request.task.TaskUpdateTitleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateTaskTool implements McpTool {

    private final TaskUpdateManager taskUpdateManager;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("update_task")
                .title("Update a task")
                .description("Changes a task's title, body, dates or assignee. "
                             + "Supply only the fields to change; anything omitted is left alone. "
                             + "Dates are the one exception: Jinear stores start and due as a pair, so supply "
                             + "both together. Use set_task_status to move a task between workflow statuses.")
                .input(McpSchemaGenerator.forInput(McpUpdateTaskInput.class))
                .output(McpSchemaGenerator.acknowledgement("taskId", "The task that was updated."))
                .write()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpUpdateTaskInput input = args.bind(McpUpdateTaskInput.class);
        if (mentionsOneDateOnly(args)) {
            return McpToolResult.error("Supply startDate and dueDate together. Jinear stores them as a pair, "
                                       + "so sending one on its own would clear the other. "
                                       + "Call get_task to read the current values first.");
        }

        boolean changed = applyTitle(input)
                          | applyDescription(input)
                          | applyAssignee(input)
                          | applyDates(input, args);

        if (!changed) {
            return McpToolResult.error("Nothing to update. Supply at least one of title, description, "
                                       + "assignedTo, startDate or dueDate.");
        }
        return McpToolResult.of(McpTaskAcknowledgementView.of(input.getTaskId()));
    }

    private boolean mentionsOneDateOnly(McpToolArguments args) {
        return args.has("startDate") != args.has("dueDate");
    }

    private boolean applyTitle(McpUpdateTaskInput input) {
        if (Objects.isNull(input.getTitle())) {
            return false;
        }
        TaskUpdateTitleRequest request = new TaskUpdateTitleRequest();
        request.setTitle(input.getTitle());
        taskUpdateManager.updateTaskTitle(input.getTaskId(), request);
        return true;
    }

    private boolean applyDescription(McpUpdateTaskInput input) {
        if (Objects.isNull(input.getDescription())) {
            return false;
        }
        TaskUpdateDescriptionRequest request = new TaskUpdateDescriptionRequest();
        request.setDescription(input.getDescription());
        taskUpdateManager.updateTaskDescription(input.getTaskId(), request);
        return true;
    }

    private boolean applyAssignee(McpUpdateTaskInput input) {
        if (Objects.isNull(input.getAssignedTo())) {
            return false;
        }
        TaskAssigneeUpdateRequest request = new TaskAssigneeUpdateRequest();
        request.setAssigneeId(input.getAssignedTo().isBlank() ? null : input.getAssignedTo());
        taskUpdateManager.updateTaskAssignee(input.getTaskId(), request);
        return true;
    }

    private boolean applyDates(McpUpdateTaskInput input, McpToolArguments args) {
        if (!args.has("startDate") && !args.has("dueDate")) {
            return false;
        }
        TaskDateUpdateRequest request = new TaskDateUpdateRequest();
        request.setAssignedDate(input.getStartDate());
        request.setDueDate(input.getDueDate());
        request.setHasPreciseAssignedDate(Objects.nonNull(input.getStartDate()));
        request.setHasPreciseDueDate(Objects.nonNull(input.getDueDate()));
        taskUpdateManager.updateTaskDates(input.getTaskId(), request);
        return true;
    }
}
