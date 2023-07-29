package co.jinear.core.manager.task;

import co.jinear.core.converter.task.TaskInitializeVoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
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
import co.jinear.core.validator.task.TaskBoardAccessValidator;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskInitializeManager {

    private final TaskInitializeService taskInitializeService;
    private final TeamRetrieveService teamRetrieveService;
    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamAccessValidator teamAccessValidator;
    private final TaskInitializeVoConverter taskInitializeVoConverter;
    private final TaskActivityService taskActivityService;
    private final TaskBoardAccessValidator taskBoardAccessValidator;
    private final TaskRetrieveService taskRetrieveService;
    private final TopicRetrieveService topicRetrieveService;

    public TaskResponse initializeTask(TaskInitializeRequest taskInitializeRequest) {
        String currentAccount = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        validateWorkspaceAccess(currentAccount, taskInitializeRequest);
        validateTeamAccess(currentAccount, taskInitializeRequest);
        validateDueDateIsAfterAssignedDate(taskInitializeRequest.getAssignedDate(), taskInitializeRequest.getDueDate());
        validateTaskBoardAccess(taskInitializeRequest, currentAccount);
        validateSubtaskAndNewTaskIsInSameTeamAndWorkspace(taskInitializeRequest);
        validateTopicAndNewTaskIsInSameTeamAndWorkspace(taskInitializeRequest);

        log.info("Initialize task has started. currentAccount: {}", currentAccount);
        TaskInitializeVo taskInitializeVo = taskInitializeVoConverter.map(taskInitializeRequest);
        taskInitializeVo.setOwnerId(currentAccount);
        TaskDto initializedTask = taskInitializeService.initializeTask(taskInitializeVo);
        retrieveAndSetTeamDto(initializedTask);
        retrieveAndSetWorkspaceDto(initializedTask);
        taskActivityService.initializeNewTaskActivity(currentAccount, currentAccountSessionId, initializedTask);
        initializeTaskBoardActivity(taskInitializeRequest, currentAccount, currentAccountSessionId, initializedTask);
        return mapResponse(initializedTask);
    }

    private void validateTopicAndNewTaskIsInSameTeamAndWorkspace(TaskInitializeRequest taskInitializeRequest) {
        String topicId = taskInitializeRequest.getTopicId();
        if (Objects.nonNull(topicId)) {
            TopicDto topicDto = topicRetrieveService.retrieve(topicId);
            if (!taskInitializeRequest.getTeamId().equalsIgnoreCase(topicDto.getTeamId()) || !taskInitializeRequest.getWorkspaceId().equalsIgnoreCase(topicDto.getWorkspaceId())) {
                throw new NotValidException();
            }
        }
    }

    private void validateSubtaskAndNewTaskIsInSameTeamAndWorkspace(TaskInitializeRequest taskInitializeRequest) {
        String subTaskOf = taskInitializeRequest.getSubTaskOf();
        if (Objects.nonNull(subTaskOf)) {
            TaskDto taskDto = taskRetrieveService.retrievePlain(subTaskOf);
            if (!taskInitializeRequest.getTeamId().equalsIgnoreCase(taskDto.getTeamId()) || !taskInitializeRequest.getWorkspaceId().equalsIgnoreCase(taskDto.getWorkspaceId())) {
                throw new NotValidException();
            }
        }
    }

    private void validateTaskBoardAccess(TaskInitializeRequest taskInitializeRequest, String currentAccount) {
        if (Objects.nonNull(taskInitializeRequest.getBoardId())) {
            taskBoardAccessValidator.validateHasTaskBoardAccess(taskInitializeRequest.getBoardId(), currentAccount);
        }
    }

    private void validateDueDateIsAfterAssignedDate(ZonedDateTime assignedDate, ZonedDateTime dueDate) {
        if (Objects.nonNull(assignedDate) && Objects.nonNull(dueDate) && assignedDate.isAfter(dueDate)) {
            throw new BusinessException();
        }
    }

    private void validateWorkspaceAccess(String accountId, TaskInitializeRequest taskInitializeRequest) {
        workspaceValidator.validateHasAccess(accountId, taskInitializeRequest.getWorkspaceId());
    }

    private void validateTeamAccess(String currentAccount, TaskInitializeRequest taskInitializeRequest) {
        teamAccessValidator.validateTeamAccess(currentAccount, taskInitializeRequest.getWorkspaceId(), taskInitializeRequest.getTeamId());
    }

    private void retrieveAndSetTeamDto(TaskDto initializedTask) {
        TeamDto teamDto = teamRetrieveService.retrieveTeam(initializedTask.getTeamId());
        initializedTask.setTeam(teamDto);
    }

    private void retrieveAndSetWorkspaceDto(TaskDto initializedTask) {
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(initializedTask.getWorkspaceId());
        initializedTask.setWorkspace(workspaceDto);
    }

    private void initializeTaskBoardActivity(TaskInitializeRequest taskInitializeRequest, String currentAccount, String currentAccountSessionId, TaskDto initializedTask) {
        String boardId = taskInitializeRequest.getBoardId();
        if (Objects.nonNull(boardId)) {
            taskActivityService.initializeTaskAddedToTaskBoardActivity(currentAccount, currentAccountSessionId, initializedTask, taskInitializeRequest.getBoardId());
        }
    }

    private TaskResponse mapResponse(TaskDto taskDto) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setTaskDto(taskDto);
        return taskResponse;
    }
}
