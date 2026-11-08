package co.jinear.core.manager.mcp.tool.board;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskBoardManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpUpdateTaskBoardInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpTaskBoardAcknowledgementView;
import co.jinear.core.model.request.task.TaskBoardUpdateColorRequest;
import co.jinear.core.model.request.task.TaskBoardUpdateDueDateRequest;
import co.jinear.core.model.request.task.TaskBoardUpdateStateRequest;
import co.jinear.core.model.request.task.TaskBoardUpdateTitleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateTaskBoardTool implements McpTool {

    private final TaskBoardManager taskBoardManager;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("update_task_board")
                .title("Update a board")
                .description("Changes a board's title, due date, state or color, or removes its due date with clearDueDate. "
                             + "Set state to CLOSED when the work on the board is done. "
                             + "Supply only the fields to change.")
                .input(McpSchemaGenerator.forInput(McpUpdateTaskBoardInput.class))
                .output(McpSchemaGenerator.acknowledgement("taskBoardId", "The board that was updated."))
                .write()
                .idempotent()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpUpdateTaskBoardInput input = args.bind(McpUpdateTaskBoardInput.class);
        if (setsAndClearsDueDate(input)) {
            return McpToolResult.error("mcp.error.task-board.due-date-conflict");
        }
        boolean titleChanged = applyTitle(input);
        boolean dueDateChanged = applyDueDate(input);
        boolean stateChanged = applyState(input);
        boolean colorChanged = applyColor(input);
        if (!(titleChanged || dueDateChanged || stateChanged || colorChanged)) {
            return McpToolResult.error("mcp.error.task-board.nothing-to-update");
        }
        return McpToolResult.of(McpTaskBoardAcknowledgementView.of(input.getTaskBoardId()));
    }

    private boolean applyTitle(McpUpdateTaskBoardInput input) {
        if (Objects.isNull(input.getTitle())) {
            return false;
        }
        TaskBoardUpdateTitleRequest request = new TaskBoardUpdateTitleRequest();
        request.setTaskBoardId(input.getTaskBoardId());
        request.setTitle(input.getTitle());
        taskBoardManager.updateTitle(request);
        return true;
    }

    private boolean setsAndClearsDueDate(McpUpdateTaskBoardInput input) {
        return clearsDueDate(input) && Objects.nonNull(input.getDueDate());
    }

    private boolean clearsDueDate(McpUpdateTaskBoardInput input) {
        return Boolean.TRUE.equals(input.getClearDueDate());
    }

    private boolean applyDueDate(McpUpdateTaskBoardInput input) {
        if (Objects.isNull(input.getDueDate()) && !clearsDueDate(input)) {
            return false;
        }
        TaskBoardUpdateDueDateRequest request = new TaskBoardUpdateDueDateRequest();
        request.setTaskBoardId(input.getTaskBoardId());
        request.setDueDate(input.getDueDate());
        taskBoardManager.updateDueDate(request);
        return true;
    }

    private boolean applyState(McpUpdateTaskBoardInput input) {
        if (Objects.isNull(input.getState())) {
            return false;
        }
        TaskBoardUpdateStateRequest request = new TaskBoardUpdateStateRequest();
        request.setTaskBoardId(input.getTaskBoardId());
        request.setState(input.getState());
        taskBoardManager.updateState(request);
        return true;
    }

    private boolean applyColor(McpUpdateTaskBoardInput input) {
        if (Objects.isNull(input.getColor())) {
            return false;
        }
        TaskBoardUpdateColorRequest request = new TaskBoardUpdateColorRequest();
        request.setTaskBoardId(input.getTaskBoardId());
        request.setColor(input.getColor());
        taskBoardManager.updateColor(request);
        return true;
    }
}
