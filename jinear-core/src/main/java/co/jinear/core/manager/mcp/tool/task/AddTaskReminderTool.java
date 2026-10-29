package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.reminder.TaskReminderManager;
import co.jinear.core.model.dto.reminder.ReminderDto;
import co.jinear.core.model.dto.task.TaskReminderScheduleDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.enumtype.reminder.RepeatType;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpAddTaskReminderInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpListView;
import co.jinear.core.model.mcp.view.McpTaskReminderView;
import co.jinear.core.model.request.reminder.TaskReminderInitializeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AddTaskReminderTool implements McpTool {

    private final TaskReminderManager taskReminderManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("add_task_reminder")
                .title("Add reminders to a task")
                .description("Adds one or more reminders to a task: at its start date, at its due date, and at a specific "
                             + "instant that can repeat. Supply at least one of atStartDate, atDueDate or remindAt. "
                             + "When a reminder fires, Jinear notifies every subscriber of the task. "
                             + "Reminders cannot be edited: to change one, remove it and add a new one. "
                             + "Returns the reminders this call created.")
                .input(McpSchemaGenerator.forInput(McpAddTaskReminderInput.class))
                .output(McpSchemaGenerator.list(McpTaskReminderView.class, "Reminders this call created."))
                .write()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpAddTaskReminderInput input = args.bind(McpAddTaskReminderInput.class);
        boolean atStartDate = Boolean.TRUE.equals(input.getAtStartDate());
        boolean atDueDate = Boolean.TRUE.equals(input.getAtDueDate());
        boolean hasRemindAt = Objects.nonNull(input.getRemindAt());
        boolean hasRepeatUntil = Objects.nonNull(input.getRepeatUntil());
        RepeatType repeat = Objects.isNull(input.getRepeat()) ? RepeatType.NONE : input.getRepeat();
        boolean repeats = !RepeatType.NONE.equals(repeat);
        if (!atStartDate && !atDueDate && !hasRemindAt) {
            return McpToolResult.error("Nothing to add. Supply at least one of atStartDate, atDueDate or remindAt.");
        }
        if (!hasRemindAt && (repeats || hasRepeatUntil)) {
            return McpToolResult.error("repeat and repeatUntil apply only to a remindAt reminder. Supply remindAt too.");
        }
        if (hasRepeatUntil && !repeats) {
            return McpToolResult.error("repeatUntil needs a repeat other than NONE.");
        }
        if (hasRepeatUntil && !input.getRepeatUntil().isAfter(input.getRemindAt())) {
            return McpToolResult.error("repeatUntil must be after remindAt.");
        }

        TaskReminderInitializeRequest request = new TaskReminderInitializeRequest();
        request.setTaskId(input.getTaskId());
        request.setBeforeAssignedDate(atStartDate);
        request.setBeforeDueDate(atDueDate);
        if (hasRemindAt) {
            request.setSpecificRemindDate(input.getRemindAt());
            request.setSpecificRemindDateRepeatType(repeat);
            request.setSpecificRemindRepeatEnd(input.getRepeatUntil());
        }
        List<ReminderDto> created;
        try {
            created = taskReminderManager.initializeTaskReminder(request).getReminders();
        } catch (BusinessException exception) {
            return McpToolResult.error("No reminder was added. A start or due date reminder needs that date on the task, "
                                       + "and a task holds at most one of each. Check them with get_task and list_task_reminders.");
        }
        List<TaskReminderScheduleDto> schedules = taskReminderManager.retrieveTaskReminderSchedules(input.getTaskId());
        List<TaskReminderScheduleDto> createdSchedules = createdSchedules(schedules, created);
        return McpToolResult.of(McpListView.of(createdSchedules, mcpViewConverter::taskReminder));
    }

    private List<TaskReminderScheduleDto> createdSchedules(List<TaskReminderScheduleDto> schedules, List<ReminderDto> created) {
        List<String> createdReminderIds = new ArrayList<>();
        for (ReminderDto reminderDto : created) {
            createdReminderIds.add(reminderDto.getReminderId());
        }
        List<TaskReminderScheduleDto> createdSchedules = new ArrayList<>();
        for (TaskReminderScheduleDto schedule : schedules) {
            if (createdReminderIds.contains(schedule.getTaskReminder().getReminderId())) {
                createdSchedules.add(schedule);
            }
        }
        return createdSchedules;
    }
}
