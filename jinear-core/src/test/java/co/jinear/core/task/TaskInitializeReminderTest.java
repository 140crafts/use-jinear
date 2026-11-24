package co.jinear.core.task;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.converter.reminder.ReminderConverter;
import co.jinear.core.converter.task.TaskInitializeVoConverter;
import co.jinear.core.converter.task.TaskUrlConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.manager.task.TaskInitializeManager;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.enumtype.reminder.RepeatType;
import co.jinear.core.model.enumtype.team.TeamStateType;
import co.jinear.core.model.request.task.TaskInitializeReminderRequest;
import co.jinear.core.model.request.task.TaskInitializeRequest;
import co.jinear.core.model.vo.task.InitializeTaskRemindersVo;
import co.jinear.core.model.vo.task.TaskInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskActivityService;
import co.jinear.core.service.task.TaskInitializeService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.reminder.TaskReminderOperationService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.topic.TopicRetrieveService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.validator.project.MilestoneValidator;
import co.jinear.core.validator.project.ProjectAccessValidator;
import co.jinear.core.validator.task.TaskBoardAccessValidator;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

class TaskInitializeReminderTest {

    private static final String ACCOUNT_ID = "account-1";
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private final TaskInitializeService taskInitializeService = Mockito.mock(TaskInitializeService.class);
    private final TeamRetrieveService teamRetrieveService = Mockito.mock(TeamRetrieveService.class);
    private final WorkspaceRetrieveService workspaceRetrieveService = Mockito.mock(WorkspaceRetrieveService.class);
    private final SessionInfoService sessionInfoService = Mockito.mock(SessionInfoService.class);
    private final TaskInitializeVoConverter taskInitializeVoConverter = Mockito.mock(TaskInitializeVoConverter.class);
    private final TaskReminderOperationService taskReminderOperationService = Mockito.mock(TaskReminderOperationService.class);
    private final TaskInitializeManager manager = manager();

    @BeforeEach
    void setUp() {
        Mockito.when(sessionInfoService.currentAccountId()).thenReturn(ACCOUNT_ID);
        Mockito.when(teamRetrieveService.checkTeamExistenceWithState("ws-1", "team-1", TeamStateType.ACTIVE)).thenReturn(true);
        Mockito.when(taskInitializeVoConverter.map(any(TaskInitializeRequest.class))).thenReturn(new TaskInitializeVo());
        Mockito.when(taskInitializeService.initializeTask(any(TaskInitializeVo.class))).thenReturn(task());
        Mockito.when(teamRetrieveService.retrieveTeam("team-1")).thenReturn(new TeamDto());
        Mockito.when(workspaceRetrieveService.retrieveWorkspaceWithId("ws-1")).thenReturn(new WorkspaceDto());
    }

    @Test
    void aTaskWithoutAReminderCreatesNoReminder() {
        manager.initializeTask(request());

        Mockito.verify(taskInitializeService).initializeTask(any(TaskInitializeVo.class));
        Mockito.verify(taskReminderOperationService, Mockito.never()).initializeTaskReminders(any());
    }

    @Test
    void aReminderThatRequestsNothingCreatesNoReminder() {
        TaskInitializeRequest request = request();
        TaskInitializeReminderRequest reminder = new TaskInitializeReminderRequest();
        reminder.setBeforeAssignedDate(false);
        reminder.setSpecificRemindDateRepeatType(RepeatType.NONE);
        request.setReminder(reminder);

        manager.initializeTask(request);

        Mockito.verify(taskReminderOperationService, Mockito.never()).initializeTaskReminders(any());
    }

    @Test
    void theRemindersAreCreatedForTheNewTask() {
        ZonedDateTime remindAt = ZonedDateTime.now().plusDays(1);
        TaskInitializeRequest request = request();
        request.setDueDate(ZonedDateTime.now().plusDays(2));
        TaskInitializeReminderRequest reminder = new TaskInitializeReminderRequest();
        reminder.setBeforeDueDate(true);
        reminder.setSpecificRemindDate(remindAt);
        reminder.setSpecificRemindDateRepeatType(RepeatType.DAILY);
        request.setReminder(reminder);

        manager.initializeTask(request);

        ArgumentCaptor<InitializeTaskRemindersVo> captor = ArgumentCaptor.forClass(InitializeTaskRemindersVo.class);
        Mockito.verify(taskReminderOperationService).initializeTaskReminders(captor.capture());
        InitializeTaskRemindersVo vo = captor.getValue();
        assertThat(vo.getTaskId()).isEqualTo("task-1");
        assertThat(vo.getOwnerId()).isEqualTo(ACCOUNT_ID);
        assertThat(vo.getBeforeDueDate()).isTrue();
        assertThat(vo.getSpecificRemindDate()).isEqualTo(remindAt);
        assertThat(vo.getSpecificRemindDateRepeatType()).isEqualTo(RepeatType.DAILY);
    }

    @Test
    void anAssignedDateReminderWithoutAnAssignedDateCreatesNoTask() {
        TaskInitializeRequest request = request();
        TaskInitializeReminderRequest reminder = new TaskInitializeReminderRequest();
        reminder.setBeforeAssignedDate(true);
        request.setReminder(reminder);

        assertThatThrownBy(() -> manager.initializeTask(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("task.init.reminder-needs-assigned-date");
        Mockito.verify(taskInitializeService, Mockito.never()).initializeTask(any());
    }

    @Test
    void aDueDateReminderWithoutADueDateCreatesNoTask() {
        TaskInitializeRequest request = request();
        TaskInitializeReminderRequest reminder = new TaskInitializeReminderRequest();
        reminder.setBeforeDueDate(true);
        request.setReminder(reminder);

        assertThatThrownBy(() -> manager.initializeTask(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("task.init.reminder-needs-due-date");
        Mockito.verify(taskInitializeService, Mockito.never()).initializeTask(any());
    }

    @Test
    void aReminderDateInThePastIsRejectedByTheRequestValidation() {
        TaskInitializeRequest request = request();
        TaskInitializeReminderRequest reminder = new TaskInitializeReminderRequest();
        reminder.setSpecificRemindDate(ZonedDateTime.now().minusHours(1));
        request.setReminder(reminder);

        Set<ConstraintViolation<TaskInitializeRequest>> violations = VALIDATOR.validate(request);

        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .containsExactly("task.reminder.initialize.date-not-future");
    }

    private TaskInitializeManager manager() {
        FeProperties feProperties = new FeProperties();
        feProperties.setTaskUrl("https://app.jinear.co/{workspaceName}/task/{taskTag}");
        return new TaskInitializeManager(
                taskInitializeService,
                teamRetrieveService,
                workspaceRetrieveService,
                sessionInfoService,
                Mockito.mock(WorkspaceValidator.class),
                Mockito.mock(TeamAccessValidator.class),
                taskInitializeVoConverter,
                new TaskUrlConverter(feProperties),
                Mockito.mock(TaskActivityService.class),
                Mockito.mock(TaskBoardAccessValidator.class),
                Mockito.mock(TaskRetrieveService.class),
                Mockito.mock(TopicRetrieveService.class),
                Mockito.mock(ProjectAccessValidator.class),
                Mockito.mock(MilestoneValidator.class),
                taskReminderOperationService,
                Mappers.getMapper(ReminderConverter.class));
    }

    private TaskInitializeRequest request() {
        TaskInitializeRequest request = new TaskInitializeRequest();
        request.setWorkspaceId("ws-1");
        request.setTeamId("team-1");
        request.setTitle("Release");
        return request;
    }

    private TaskDto task() {
        TaskDto task = new TaskDto();
        task.setTaskId("task-1");
        task.setWorkspaceId("ws-1");
        task.setTeamId("team-1");
        return task;
    }
}
