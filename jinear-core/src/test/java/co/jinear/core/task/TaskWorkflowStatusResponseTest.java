package co.jinear.core.task;

import co.jinear.core.converter.task.TaskDtoConverter;
import co.jinear.core.converter.task.TaskSubscriptionConverter;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.UpdateTaskWorkflowDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.vo.task.TaskInitializeVo;
import co.jinear.core.repository.TaskRepository;
import co.jinear.core.service.richtext.RichTextInitializeService;
import co.jinear.core.service.task.TaskAnalyticsService;
import co.jinear.core.service.task.TaskFtsRefreshService;
import co.jinear.core.service.task.TaskInitializeService;
import co.jinear.core.service.task.TaskLockService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.TaskUpdateService;
import co.jinear.core.service.task.board.entry.TaskBoardEntryOperationService;
import co.jinear.core.service.task.feed.TaskFeedItemOperationService;
import co.jinear.core.service.task.relation.TaskRelationInitializeService;
import co.jinear.core.service.task.reminder.TaskReminderDateUpdateService;
import co.jinear.core.service.task.reminder.TaskReminderOperationService;
import co.jinear.core.service.task.subscription.TaskSubscriptionOperationService;
import co.jinear.core.service.team.TeamLockService;
import co.jinear.core.service.team.workflow.TeamWorkflowStatusRetrieveService;
import co.jinear.core.service.topic.TopicSequenceService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class TaskWorkflowStatusResponseTest {

    private final TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
    private final TaskRetrieveService taskRetrieveService = Mockito.mock(TaskRetrieveService.class);
    private final TeamWorkflowStatusRetrieveService workflowStatusRetrieveService = Mockito.mock(TeamWorkflowStatusRetrieveService.class);
    private final TaskDtoConverter taskDtoConverter = Mockito.mock(TaskDtoConverter.class);
    private final TaskAnalyticsService taskAnalyticsService = Mockito.mock(TaskAnalyticsService.class);

    private TeamWorkflowStatusDto status(String name, TeamWorkflowStateGroup group) {
        TeamWorkflowStatusDto status = new TeamWorkflowStatusDto();
        status.setName(name);
        status.setWorkflowStateGroup(group);
        return status;
    }

    private TaskUpdateService taskUpdateService() {
        return new TaskUpdateService(
                taskRetrieveService,
                taskRepository,
                workflowStatusRetrieveService,
                Mockito.mock(RichTextInitializeService.class),
                Mockito.mock(TaskLockService.class),
                Mockito.mock(TopicSequenceService.class),
                Mockito.mock(TaskSubscriptionConverter.class),
                Mockito.mock(TaskSubscriptionOperationService.class),
                Mockito.mock(TaskReminderDateUpdateService.class),
                Mockito.mock(TaskReminderOperationService.class),
                Mockito.mock(TaskFtsRefreshService.class),
                taskDtoConverter);
    }

    private TaskInitializeService taskInitializeService() {
        return new TaskInitializeService(
                taskRepository,
                Mockito.mock(TaskLockService.class),
                Mockito.mock(TeamLockService.class),
                workflowStatusRetrieveService,
                Mockito.mock(TopicSequenceService.class),
                Mockito.mock(RichTextInitializeService.class),
                taskDtoConverter,
                Mockito.mock(TaskRelationInitializeService.class),
                Mockito.mock(TaskSubscriptionConverter.class),
                Mockito.mock(TaskSubscriptionOperationService.class),
                Mockito.mock(TaskBoardEntryOperationService.class),
                Mockito.mock(TaskFeedItemOperationService.class),
                taskAnalyticsService,
                Mockito.mock(TaskFtsRefreshService.class));
    }

    @Test
    void aStatusChangeAnswersWithTheNewStatusNotTheOneLoadedBeforeIt() {
        TeamWorkflowStatusDto started = status("Started", TeamWorkflowStateGroup.STARTED);
        Task task = new Task();
        Mockito.when(workflowStatusRetrieveService.retrieve("status-started")).thenReturn(started);
        Mockito.when(taskRetrieveService.retrieveEntity("task-1")).thenReturn(task);
        Mockito.when(taskRepository.save(task)).thenReturn(task);
        Mockito.when(taskDtoConverter.map(task)).thenReturn(new TaskDto());
        Mockito.when(taskDtoConverter.map(ArgumentMatchers.any(TaskDto.class), ArgumentMatchers.any()))
                .thenAnswer(invocation -> {
                    UpdateTaskWorkflowDto updated = new UpdateTaskWorkflowDto();
                    updated.setTaskDto(invocation.getArgument(0));
                    return updated;
                });

        UpdateTaskWorkflowDto updated = taskUpdateService().updateTaskWorkflow("task-1", "status-started");

        assertThat(updated.getTaskDto().getWorkflowStatus()).isSameAs(started);
    }

    @Test
    void aNewTaskAnswersWithItsInitialStatus() {
        TeamWorkflowStatusDto backlog = status("Backlog", TeamWorkflowStateGroup.BACKLOG);
        TaskInitializeVo taskInitializeVo = new TaskInitializeVo();
        taskInitializeVo.setWorkspaceId("workspace-1");
        taskInitializeVo.setTeamId("team-1");
        taskInitializeVo.setOwnerId("account-1");
        taskInitializeVo.setTitle("Ship the release");
        Mockito.when(taskAnalyticsService.retrieveMaxTeamTagNo("team-1")).thenReturn(5L);
        Mockito.when(workflowStatusRetrieveService.retrieveFirstFromGroup("team-1", TeamWorkflowStateGroup.BACKLOG)).thenReturn(backlog);
        Mockito.when(taskRepository.saveAndFlush(ArgumentMatchers.any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(taskDtoConverter.map(ArgumentMatchers.any(Task.class))).thenReturn(new TaskDto());

        TaskDto created = taskInitializeService().initializeTask(taskInitializeVo);

        assertThat(created.getWorkflowStatus()).isSameAs(backlog);
    }
}
