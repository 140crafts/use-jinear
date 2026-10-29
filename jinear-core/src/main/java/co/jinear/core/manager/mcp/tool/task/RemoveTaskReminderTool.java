package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.reminder.TaskReminderManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpRemoveTaskReminderInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpTaskReminderAcknowledgementView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveTaskReminderTool implements McpTool {

    private final TaskReminderManager taskReminderManager;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("remove_task_reminder")
                .title("Remove a task reminder")
                .description("Removes a reminder from a task, so it does not fire again for anyone. "
                             + "A repeating reminder stops for all its future instants. Get the id from list_task_reminders.")
                .input(McpSchemaGenerator.forInput(McpRemoveTaskReminderInput.class))
                .output(McpSchemaGenerator.forType(McpTaskReminderAcknowledgementView.class))
                .destructive()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpRemoveTaskReminderInput input = args.bind(McpRemoveTaskReminderInput.class);
        taskReminderManager.passivizeTaskReminder(input.getTaskReminderId());
        return McpToolResult.of(McpTaskReminderAcknowledgementView.of(input.getTaskReminderId()));
    }
}
