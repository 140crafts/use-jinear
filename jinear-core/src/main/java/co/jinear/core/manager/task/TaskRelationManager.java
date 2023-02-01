package co.jinear.core.manager.task;

import co.jinear.core.converter.task.TaskRelationInitializeVoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.request.task.TaskRelationInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.task.TaskRelationInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskRelationInitializeService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.validator.task.TaskAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskRelationManager {

    private final TaskRelationInitializeService taskRelationInitializeService;
    private final TaskAccessValidator validateTaskAccess;
    private final SessionInfoService sessionInfoService;
    private final TaskRelationInitializeVoConverter taskRelationInitializeVoConverter;
    private final TaskRetrieveService taskRetrieveService;

    public BaseResponse initializeTaskRelation(TaskRelationInitializeRequest taskRelationInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateTaskAccess.validateTaskAccess(currentAccountId, taskRelationInitializeRequest.getTaskId());
        TaskDto relatedTaskDto = taskRetrieveService.retrievePlain(taskRelationInitializeRequest.getRelatedTaskId());
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskRelationInitializeRequest.getTaskId());
        validateTasksSameTeamAndWorkspace(relatedTaskDto, taskDto);

        TaskRelationInitializeVo taskRelationInitializeVo = taskRelationInitializeVoConverter.map(taskRelationInitializeRequest, currentAccountId, taskDto);
        taskRelationInitializeService.initializeTaskRelation(taskRelationInitializeVo);
        return new BaseResponse();
    }

    private void validateTasksSameTeamAndWorkspace(TaskDto relatedTaskDto, TaskDto taskDto) {
        if (!Objects.equals(relatedTaskDto.getTeamId(), taskDto.getTeamId()) || !Objects.equals(relatedTaskDto.getWorkspaceId(), taskDto.getWorkspaceId())) {
            throw new BusinessException();
        }
    }
}
