package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.reminder.TaskReminderManager;
import co.jinear.core.model.dto.reminder.ReminderDto;
import co.jinear.core.model.dto.task.TaskReminderDto;
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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        Optional<String> inputError = findInputError(input);
        return inputError
                .map(McpToolResult::error)
                .orElseGet(() -> addReminders(input));
    }

    private Optional<String> findInputError(McpAddTaskReminderInput input) {
        boolean hasRemindAt = Objects.nonNull(input.getRemindAt());
        boolean hasRepeatUntil = Objects.nonNull(input.getRepeatUntil());
        boolean repeats = !RepeatType.NONE.equals(repeatOf(input));
        if (!Boolean.TRUE.equals(input.getAtStartDate()) && !Boolean.TRUE.equals(input.getAtDueDate()) && !hasRemindAt) {
            return Optional.of("Nothing to add. Supply at least one of atStartDate, atDueDate or remindAt.");
        }
        if (!hasRemindAt && (repeats || hasRepeatUntil)) {
            return Optional.of("repeat and repeatUntil apply only to a remindAt reminder. Supply remindAt too.");
        }
        if (hasRepeatUntil && !repeats) {
            return Optional.of("repeatUntil needs a repeat other than NONE.");
        }
        if (hasRepeatUntil && !input.getRepeatUntil().isAfter(input.getRemindAt())) {
            return Optional.of("repeatUntil must be after remindAt.");
        }
        return Optional.empty();
    }

    private McpToolResult addReminders(McpAddTaskReminderInput input) {
        TaskReminderInitializeRequest request = mapRequest(input);
        List<ReminderDto> created;
        try {
            created = taskReminderManager.initializeTaskReminder(request).getReminders();
        } catch (BusinessException exception) {
            return McpToolResult.error("No reminder was added. A start or due date reminder needs that date on the task, "
                                       + "and a task holds at most one of each. Check them with get_task and list_task_reminders.");
        }
        List<TaskReminderScheduleDto> createdSchedules = retrieveCreatedSchedules(input.getTaskId(), created);
        McpListView<McpTaskReminderView> view = McpListView.of(createdSchedules, mcpViewConverter::taskReminder);
        return McpToolResult.of(view);
    }

    private TaskReminderInitializeRequest mapRequest(McpAddTaskReminderInput input) {
        TaskReminderInitializeRequest request = new TaskReminderInitializeRequest();
        request.setTaskId(input.getTaskId());
        request.setBeforeAssignedDate(Boolean.TRUE.equals(input.getAtStartDate()));
        request.setBeforeDueDate(Boolean.TRUE.equals(input.getAtDueDate()));
        if (Objects.nonNull(input.getRemindAt())) {
            request.setSpecificRemindDate(input.getRemindAt());
            request.setSpecificRemindDateRepeatType(repeatOf(input));
            request.setSpecificRemindRepeatEnd(input.getRepeatUntil());
        }
        return request;
    }

    private RepeatType repeatOf(McpAddTaskReminderInput input) {
        return Optional.ofNullable(input.getRepeat()).orElse(RepeatType.NONE);
    }

    private List<TaskReminderScheduleDto> retrieveCreatedSchedules(String taskId, List<ReminderDto> created) {
        Map<String, ReminderDto> createdById = created.stream()
                .collect(Collectors.toMap(ReminderDto::getReminderId, Function.identity()));
        List<TaskReminderScheduleDto> schedules = taskReminderManager.retrieveTaskReminderSchedules(taskId);
        return schedules.stream()
                .filter(schedule -> createdById.containsKey(schedule.getTaskReminder().getReminderId()))
                .map(schedule -> attachCreatedReminder(schedule, createdById))
                .toList();
    }

    private TaskReminderScheduleDto attachCreatedReminder(TaskReminderScheduleDto schedule, Map<String, ReminderDto> createdById) {
        TaskReminderDto taskReminder = schedule.getTaskReminder();
        ReminderDto reminder = createdById.get(taskReminder.getReminderId());
        taskReminder.setReminder(reminder);
        return schedule;
    }
}
