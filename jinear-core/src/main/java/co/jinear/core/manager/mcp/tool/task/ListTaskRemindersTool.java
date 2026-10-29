package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.reminder.TaskReminderManager;
import co.jinear.core.model.dto.task.TaskReminderScheduleDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpListTaskRemindersInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpListView;
import co.jinear.core.model.mcp.view.McpTaskReminderView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListTaskRemindersTool implements McpTool {

    private final TaskReminderManager taskReminderManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_task_reminders")
                .title("List a task's reminders")
                .description("Lists the reminders on a task, oldest first, with the next instant each one fires. "
                             + "Reminders belong to the task: when one fires, Jinear notifies every subscriber of the task "
                             + "by push notification and email. Use it before add_task_reminder to avoid duplicates, "
                             + "and to find the id for remove_task_reminder.")
                .input(McpSchemaGenerator.forInput(McpListTaskRemindersInput.class))
                .output(McpSchemaGenerator.list(McpTaskReminderView.class, "Reminders on this task."))
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListTaskRemindersInput input = args.bind(McpListTaskRemindersInput.class);
        List<TaskReminderScheduleDto> schedules = taskReminderManager.retrieveTaskReminderSchedules(input.getTaskId());
        return McpToolResult.of(McpListView.of(schedules, mcpViewConverter::taskReminder));
    }
}
