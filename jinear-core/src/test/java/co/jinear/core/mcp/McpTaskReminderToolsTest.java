package co.jinear.core.mcp;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.converter.reminder.ReminderConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.task.AddTaskReminderTool;
import co.jinear.core.manager.mcp.tool.task.ListTaskRemindersTool;
import co.jinear.core.manager.mcp.tool.task.RemoveTaskReminderTool;
import co.jinear.core.manager.reminder.TaskReminderManager;
import co.jinear.core.model.dto.reminder.ReminderDto;
import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.dto.task.TaskReminderDto;
import co.jinear.core.model.dto.task.TaskReminderScheduleDto;
import co.jinear.core.model.enumtype.reminder.ReminderJobStatus;
import co.jinear.core.model.enumtype.reminder.RepeatType;
import co.jinear.core.model.enumtype.task.TaskReminderType;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolException;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.view.McpListView;
import co.jinear.core.model.mcp.view.McpTaskReminderAcknowledgementView;
import co.jinear.core.model.mcp.view.McpTaskReminderView;
import co.jinear.core.model.request.reminder.TaskReminderInitializeRequest;
import co.jinear.core.model.response.reminder.ReminderResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.reminder.job.ReminderJobRetrieveService;
import co.jinear.core.service.task.reminder.TaskReminderOperationService;
import co.jinear.core.service.task.reminder.TaskReminderRetrieveService;
import co.jinear.core.validator.task.TaskAccessValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class McpTaskReminderToolsTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    private static final ZonedDateTime START_DATE = ZonedDateTime.parse("2099-01-05T09:00:00Z");
    private static final ZonedDateTime WEEKLY_NEXT = ZonedDateTime.parse("2099-01-12T09:00:00+03:00");
    private static final ZonedDateTime WEEKLY_END = ZonedDateTime.parse("2099-03-01T09:00:00Z");

    private final TaskReminderManager taskReminderManager = Mockito.mock(TaskReminderManager.class);
    private final McpViewConverter viewConverter = new McpViewConverter();

    private McpToolArguments args(String json) throws Exception {
        return McpToolArguments.of(OBJECT_MAPPER.readTree(json), OBJECT_MAPPER, VALIDATOR);
    }

    private McpToolContext context() {
        return McpToolContext.builder().accountId("account-1").scopes(Set.of()).build();
    }

    private McpToolResult list(String json) throws Exception {
        return new ListTaskRemindersTool(taskReminderManager, viewConverter).call(context(), args(json));
    }

    private McpToolResult add(String json) throws Exception {
        return new AddTaskReminderTool(taskReminderManager, viewConverter).call(context(), args(json));
    }

    private McpToolResult remove(String json) throws Exception {
        return new RemoveTaskReminderTool(taskReminderManager).call(context(), args(json));
    }

    private static TaskReminderDto taskReminder(String taskReminderId, String reminderId, TaskReminderType type,
                                                RepeatType repeatType, ZonedDateTime repeatEnd) {
        ReminderDto reminder = new ReminderDto();
        reminder.setReminderId(reminderId);
        reminder.setOwnerId("account-1");
        reminder.setRelatedObjectId("task-1");
        reminder.setRepeatType(repeatType);
        reminder.setRepeatEnd(repeatEnd);
        TaskReminderDto taskReminder = new TaskReminderDto();
        taskReminder.setTaskReminderId(taskReminderId);
        taskReminder.setTaskId("task-1");
        taskReminder.setReminderId(reminderId);
        taskReminder.setTaskReminderType(type);
        taskReminder.setReminder(reminder);
        return taskReminder;
    }

    private static TaskReminderScheduleDto schedule(TaskReminderDto taskReminder, ZonedDateTime nextRemindDate) {
        TaskReminderScheduleDto schedule = new TaskReminderScheduleDto();
        schedule.setTaskReminder(taskReminder);
        schedule.setNextRemindDate(nextRemindDate);
        return schedule;
    }

    private static List<TaskReminderScheduleDto> schedules() {
        TaskReminderDto start = taskReminder("tr-start", "r-start", TaskReminderType.ASSIGNED_DATE, RepeatType.NONE, null);
        TaskReminderDto weekly = taskReminder("tr-weekly", "r-weekly", TaskReminderType.SPECIFIC_DATE, RepeatType.WEEKLY, WEEKLY_END);
        TaskReminderDto due = taskReminder("tr-due", "r-due", TaskReminderType.DUE_DATE, RepeatType.NONE, null);
        return List.of(schedule(start, START_DATE), schedule(weekly, WEEKLY_NEXT), schedule(due, null));
    }

    private static ReminderResponse created(String... reminderIds) {
        List<ReminderDto> reminders = new ArrayList<>();
        for (String reminderId : reminderIds) {
            ReminderDto reminder = new ReminderDto();
            reminder.setReminderId(reminderId);
            reminders.add(reminder);
        }
        ReminderResponse response = new ReminderResponse();
        response.setReminders(reminders);
        return response;
    }

    @SuppressWarnings("unchecked")
    private static List<McpTaskReminderView> items(McpToolResult result) {
        return ((McpListView<McpTaskReminderView>) result.getStructuredContent()).getItems();
    }

    private TaskReminderInitializeRequest sentRequest() {
        ArgumentCaptor<TaskReminderInitializeRequest> captor = ArgumentCaptor.forClass(TaskReminderInitializeRequest.class);
        Mockito.verify(taskReminderManager).initializeTaskReminder(captor.capture());
        return captor.getValue();
    }

    @Test
    void listMapsEveryReminderWithItsNextInstant() throws Exception {
        Mockito.when(taskReminderManager.retrieveTaskReminderSchedules("task-1")).thenReturn(schedules());

        McpToolResult result = list("{\"taskId\":\"task-1\"}");

        List<McpTaskReminderView> items = items(result);
        assertThat(result.isError()).isFalse();
        assertThat(items).extracting(McpTaskReminderView::getTaskReminderId).containsExactly("tr-start", "tr-weekly", "tr-due");
        assertThat(items).extracting(McpTaskReminderView::getType).containsExactly("START_DATE", "SPECIFIC_DATE", "DUE_DATE");
        McpTaskReminderView weekly = items.get(1);
        assertThat(weekly.getTaskId()).isEqualTo("task-1");
        assertThat(weekly.getRepeat()).isEqualTo("WEEKLY");
        assertThat(weekly.getRepeatUntil()).isEqualTo("2099-03-01T09:00:00Z");
        assertThat(weekly.getNextRemindAt()).isEqualTo("2099-01-12T06:00:00Z");
        assertThat(weekly.getCreatedByAccountId()).isEqualTo("account-1");
        assertThat(items.get(0).getRepeatUntil()).isNull();
        assertThat(items.get(2).getNextRemindAt()).isNull();
    }

    @Test
    void addSendsEveryRequestedReminderAndReturnsOnlyTheCreatedOnes() throws Exception {
        Mockito.when(taskReminderManager.initializeTaskReminder(Mockito.any())).thenReturn(created("r-start", "r-weekly"));
        Mockito.when(taskReminderManager.retrieveTaskReminderSchedules("task-1")).thenReturn(schedules());

        McpToolResult result = add("{\"taskId\":\"task-1\",\"atStartDate\":true,\"remindAt\":\"2099-01-05T09:00:00+03:00\","
                                   + "\"repeat\":\"WEEKLY\",\"repeatUntil\":\"2099-03-01T09:00:00Z\"}");

        TaskReminderInitializeRequest request = sentRequest();
        assertThat(request.getTaskId()).isEqualTo("task-1");
        assertThat(request.getBeforeAssignedDate()).isTrue();
        assertThat(request.getBeforeDueDate()).isFalse();
        assertThat(request.getSpecificRemindDate()).isEqualTo(ZonedDateTime.parse("2099-01-05T09:00:00+03:00"));
        assertThat(request.getSpecificRemindDateRepeatType()).isEqualTo(RepeatType.WEEKLY);
        assertThat(request.getSpecificRemindRepeatEnd()).isEqualTo(WEEKLY_END);
        assertThat(result.isError()).isFalse();
        assertThat(items(result)).extracting(McpTaskReminderView::getTaskReminderId).containsExactly("tr-start", "tr-weekly");
    }

    @Test
    void addDefaultsTheRepeatOfASpecificReminderToNone() throws Exception {
        Mockito.when(taskReminderManager.initializeTaskReminder(Mockito.any())).thenReturn(created("r-weekly"));
        Mockito.when(taskReminderManager.retrieveTaskReminderSchedules("task-1")).thenReturn(schedules());

        add("{\"taskId\":\"task-1\",\"remindAt\":\"2099-01-05T09:00:00Z\"}");

        TaskReminderInitializeRequest request = sentRequest();
        assertThat(request.getSpecificRemindDateRepeatType()).isEqualTo(RepeatType.NONE);
        assertThat(request.getSpecificRemindRepeatEnd()).isNull();
        assertThat(request.getBeforeAssignedDate()).isFalse();
    }

    @Test
    void addWithOnlyDateRemindersLeavesTheSpecificFieldsEmpty() throws Exception {
        Mockito.when(taskReminderManager.initializeTaskReminder(Mockito.any())).thenReturn(created("r-due"));
        Mockito.when(taskReminderManager.retrieveTaskReminderSchedules("task-1")).thenReturn(schedules());

        McpToolResult result = add("{\"taskId\":\"task-1\",\"atDueDate\":true}");

        TaskReminderInitializeRequest request = sentRequest();
        assertThat(request.getBeforeDueDate()).isTrue();
        assertThat(request.getSpecificRemindDate()).isNull();
        assertThat(request.getSpecificRemindDateRepeatType()).isNull();
        assertThat(items(result)).extracting(McpTaskReminderView::getType).containsExactly("DUE_DATE");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"taskId\":\"task-1\"}",
            "{\"taskId\":\"task-1\",\"atStartDate\":false,\"atDueDate\":false}",
            "{\"taskId\":\"task-1\",\"atDueDate\":true,\"repeat\":\"DAILY\"}",
            "{\"taskId\":\"task-1\",\"atDueDate\":true,\"repeatUntil\":\"2099-03-01T09:00:00Z\"}",
            "{\"taskId\":\"task-1\",\"remindAt\":\"2099-01-05T09:00:00Z\",\"repeatUntil\":\"2099-03-01T09:00:00Z\"}",
            "{\"taskId\":\"task-1\",\"remindAt\":\"2099-01-05T09:00:00Z\",\"repeat\":\"NONE\",\"repeatUntil\":\"2099-03-01T09:00:00Z\"}",
            "{\"taskId\":\"task-1\",\"remindAt\":\"2099-03-01T09:00:00Z\",\"repeat\":\"DAILY\",\"repeatUntil\":\"2099-01-05T09:00:00Z\"}"
    })
    void addRefusesAnInconsistentRequestWithoutCallingTheManager(String json) throws Exception {
        McpToolResult result = add(json);

        assertThat(result.isError()).isTrue();
        Mockito.verifyNoInteractions(taskReminderManager);
    }

    @Test
    void addRefusesAPastInstant() {
        assertThatThrownBy(() -> add("{\"taskId\":\"task-1\",\"remindAt\":\"2001-01-05T09:00:00Z\"}"))
                .isInstanceOf(McpToolException.class)
                .hasMessageContaining("remindAt");
        Mockito.verifyNoInteractions(taskReminderManager);
    }

    @Test
    void addExplainsWhyNothingWasCreated() throws Exception {
        Mockito.when(taskReminderManager.initializeTaskReminder(Mockito.any())).thenThrow(new BusinessException());

        McpToolResult result = add("{\"taskId\":\"task-1\",\"atStartDate\":true}");

        assertThat(result.isError()).isTrue();
        assertThat(result.getText()).contains("No reminder was added");
        Mockito.verify(taskReminderManager, Mockito.never()).retrieveTaskReminderSchedules(Mockito.any());
    }

    @Test
    void removePassivizesTheReminder() throws Exception {
        McpToolResult result = remove("{\"taskReminderId\":\"tr-start\"}");

        Mockito.verify(taskReminderManager).passivizeTaskReminder("tr-start");
        McpTaskReminderAcknowledgementView view = (McpTaskReminderAcknowledgementView) result.getStructuredContent();
        assertThat(view.getTaskReminderId()).isEqualTo("tr-start");
        assertThat(view.getOk()).isTrue();
    }

    @Test
    void managerReadsTheNextPendingJobOfEachReminder() {
        SessionInfoService sessionInfoService = Mockito.mock(SessionInfoService.class);
        TaskAccessValidator taskAccessValidator = Mockito.mock(TaskAccessValidator.class);
        TaskReminderRetrieveService taskReminderRetrieveService = Mockito.mock(TaskReminderRetrieveService.class);
        ReminderJobRetrieveService reminderJobRetrieveService = Mockito.mock(ReminderJobRetrieveService.class);
        TaskReminderManager manager = new TaskReminderManager(Mockito.mock(ReminderConverter.class), sessionInfoService,
                taskAccessValidator, Mockito.mock(TaskReminderOperationService.class), taskReminderRetrieveService, reminderJobRetrieveService);
        TaskReminderDto start = taskReminder("tr-start", "r-start", TaskReminderType.ASSIGNED_DATE, RepeatType.NONE, null);
        TaskReminderDto fired = taskReminder("tr-fired", "r-fired", TaskReminderType.SPECIFIC_DATE, RepeatType.NONE, null);
        ReminderJobDto pendingJob = new ReminderJobDto();
        pendingJob.setDate(START_DATE);
        Mockito.when(sessionInfoService.currentAccountId()).thenReturn("account-1");
        Mockito.when(taskReminderRetrieveService.retrieveAllWithRelatedTask("task-1")).thenReturn(List.of(start, fired));
        Mockito.when(reminderJobRetrieveService.findFirstReminderJobOptional("r-start", ReminderJobStatus.PENDING)).thenReturn(Optional.of(pendingJob));
        Mockito.when(reminderJobRetrieveService.findFirstReminderJobOptional("r-fired", ReminderJobStatus.PENDING)).thenReturn(Optional.empty());

        List<TaskReminderScheduleDto> schedules = manager.retrieveTaskReminderSchedules("task-1");

        Mockito.verify(taskAccessValidator).validateTaskAccess("account-1", "task-1");
        assertThat(schedules).extracting(TaskReminderScheduleDto::getTaskReminder).containsExactly(start, fired);
        assertThat(schedules).extracting(TaskReminderScheduleDto::getNextRemindDate).containsExactly(START_DATE, null);
    }

    @Test
    void managerReadsNothingWithoutTaskAccess() {
        SessionInfoService sessionInfoService = Mockito.mock(SessionInfoService.class);
        TaskAccessValidator taskAccessValidator = Mockito.mock(TaskAccessValidator.class);
        TaskReminderRetrieveService taskReminderRetrieveService = Mockito.mock(TaskReminderRetrieveService.class);
        TaskReminderManager manager = new TaskReminderManager(Mockito.mock(ReminderConverter.class), sessionInfoService,
                taskAccessValidator, Mockito.mock(TaskReminderOperationService.class), taskReminderRetrieveService,
                Mockito.mock(ReminderJobRetrieveService.class));
        Mockito.when(sessionInfoService.currentAccountId()).thenReturn("account-1");
        Mockito.doThrow(new NoAccessException()).when(taskAccessValidator).validateTaskAccess("account-1", "task-1");

        assertThatThrownBy(() -> manager.retrieveTaskReminderSchedules("task-1")).isInstanceOf(NoAccessException.class);
        Mockito.verifyNoInteractions(taskReminderRetrieveService);
    }
}
