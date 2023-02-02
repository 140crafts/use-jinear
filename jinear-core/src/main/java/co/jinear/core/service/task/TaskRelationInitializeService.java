package co.jinear.core.service.task;

import co.jinear.core.converter.task.TaskRelationConverter;
import co.jinear.core.model.dto.task.TaskRelationDto;
import co.jinear.core.model.entity.task.TaskRelation;
import co.jinear.core.model.vo.task.TaskRelationInitializeVo;
import co.jinear.core.model.vo.workspace.WorkspaceActivityCreateVo;
import co.jinear.core.repository.TaskRelationRepository;
import co.jinear.core.service.workspace.activity.WorkspaceActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static co.jinear.core.model.enumtype.workspace.WorkspaceActivityType.RELATION_INITIALIZED;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskRelationInitializeService {

    private final TaskRelationRepository taskRelationRepository;
    private final TaskRelationConverter taskRelationConverter;
    private final WorkspaceActivityService workspaceActivityService;

    @Transactional
    public TaskRelationDto initializeTaskRelation(TaskRelationInitializeVo taskRelationInitializeVo) {
        log.info("Initialize task relation has started. taskRelationInitializeVo: {}", taskRelationInitializeVo);
        TaskRelation relation = taskRelationConverter.map(taskRelationInitializeVo);
        TaskRelation saved = taskRelationRepository.save(relation);
        log.info("New relation initialized. taskRelationId: {}", saved.getTaskRelationId());
        initializeTaskRelationInitializedActivity(relation, taskRelationInitializeVo);
        return taskRelationConverter.map(saved);
    }

    private void initializeTaskRelationInitializedActivity(TaskRelation relation, TaskRelationInitializeVo taskRelationInitializeVo) {
        WorkspaceActivityCreateVo vo = WorkspaceActivityCreateVo
                .builder()
                .workspaceId(taskRelationInitializeVo.getWorkspaceId())
                .teamId(taskRelationInitializeVo.getTeamId())
                .taskId(taskRelationInitializeVo.getTaskId())
                .performedBy(taskRelationInitializeVo.getPerformedBy())
                .relatedObjectId(relation.getTaskRelationId())
                .type(RELATION_INITIALIZED)
                .build();
        workspaceActivityService.createWorkspaceActivity(vo);
    }
}
