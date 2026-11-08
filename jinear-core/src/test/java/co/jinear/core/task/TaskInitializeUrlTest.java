package co.jinear.core.task;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.converter.task.TaskInitializeVoConverter;
import co.jinear.core.converter.task.TaskUrlConverter;
import co.jinear.core.manager.task.TaskInitializeManager;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.enumtype.team.TeamStateType;
import co.jinear.core.model.request.task.TaskInitializeRequest;
import co.jinear.core.model.response.task.TaskResponse;
import co.jinear.core.model.vo.task.TaskInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskActivityService;
import co.jinear.core.service.task.TaskInitializeService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.topic.TopicRetrieveService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.validator.project.MilestoneValidator;
import co.jinear.core.validator.project.ProjectAccessValidator;
import co.jinear.core.validator.task.TaskBoardAccessValidator;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class TaskInitializeUrlTest {

    private final TaskInitializeService taskInitializeService = Mockito.mock(TaskInitializeService.class);
    private final TeamRetrieveService teamRetrieveService = Mockito.mock(TeamRetrieveService.class);
    private final WorkspaceRetrieveService workspaceRetrieveService = Mockito.mock(WorkspaceRetrieveService.class);
    private final TaskInitializeVoConverter taskInitializeVoConverter = Mockito.mock(TaskInitializeVoConverter.class);

    @Test
    void theCreatedTaskUrlUsesTheWorkspaceNameAndTheTaskReference() {
        TaskInitializeManager manager = manager();
        Mockito.when(teamRetrieveService.checkTeamExistenceWithState("ws-1", "team-1", TeamStateType.ACTIVE)).thenReturn(true);
        Mockito.when(taskInitializeVoConverter.map(any(TaskInitializeRequest.class))).thenReturn(new TaskInitializeVo());
        Mockito.when(taskInitializeService.initializeTask(any(TaskInitializeVo.class))).thenReturn(taskWithoutRelations());
        Mockito.when(teamRetrieveService.retrieveTeam("team-1")).thenReturn(team());
        Mockito.when(workspaceRetrieveService.retrieveWorkspaceWithId("ws-1")).thenReturn(workspace());

        TaskResponse response = manager.initializeTask(request());

        assertThat(response.getTaskDto().getUrl()).isEqualTo("https://app.jinear.co/acme/task/CGD-2");
    }

    private TaskInitializeManager manager() {
        FeProperties feProperties = new FeProperties();
        feProperties.setTaskUrl("https://app.jinear.co/{workspaceName}/task/{taskTag}");
        return new TaskInitializeManager(
                taskInitializeService,
                teamRetrieveService,
                workspaceRetrieveService,
                Mockito.mock(SessionInfoService.class),
                Mockito.mock(WorkspaceValidator.class),
                Mockito.mock(TeamAccessValidator.class),
                taskInitializeVoConverter,
                new TaskUrlConverter(feProperties),
                Mockito.mock(TaskActivityService.class),
                Mockito.mock(TaskBoardAccessValidator.class),
                Mockito.mock(TaskRetrieveService.class),
                Mockito.mock(TopicRetrieveService.class),
                Mockito.mock(ProjectAccessValidator.class),
                Mockito.mock(MilestoneValidator.class));
    }

    private TaskInitializeRequest request() {
        TaskInitializeRequest request = new TaskInitializeRequest();
        request.setWorkspaceId("ws-1");
        request.setTeamId("team-1");
        request.setTitle("Release");
        return request;
    }

    private TaskDto taskWithoutRelations() {
        TaskDto task = new TaskDto();
        task.setTaskId("task-1");
        task.setWorkspaceId("ws-1");
        task.setTeamId("team-1");
        task.setTeamTagNo(2);
        task.setUrl("https://app.jinear.co//task/task-1");
        return task;
    }

    private TeamDto team() {
        TeamDto team = new TeamDto();
        team.setTag("CGD");
        return team;
    }

    private WorkspaceDto workspace() {
        WorkspaceDto workspace = new WorkspaceDto();
        workspace.setUsername("acme");
        return workspace;
    }
}
