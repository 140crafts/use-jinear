package co.jinear.core.service.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.entity.task.TaskRelation;
import co.jinear.core.model.vo.workspace.WorkspaceActivityCreateVo;
import co.jinear.core.repository.TaskRelationRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.workspace.activity.WorkspaceActivityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static co.jinear.core.model.enumtype.workspace.WorkspaceActivityType.RELATION_REMOVED;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskRelationRemovalService {

    private final TaskRelationRepository taskRelationRepository;
    private final TaskRelationRetrieveService taskRelationRetrieveService;
    private final TaskRetrieveService taskRetrieveService;
    private final WorkspaceActivityService workspaceActivityService;
    private final PassiveService passiveService;

    @Transactional
    public void removeTaskRelation(String relationId, String performedByAccountId) {
        log.info("Remove task relation has started for relationId: {}, accountId: {}", relationId, performedByAccountId);
        TaskRelation saved = passivizeEntity(relationId, performedByAccountId);
        initializeTaskRelationInitializedActivity(saved, performedByAccountId);
        log.info("Remove task relation has finished for relationId: {}, accountId: {}, passiveId: {}", relationId, performedByAccountId, saved.getPassiveId());
    }

    private TaskRelation passivizeEntity(String relationId, String performedByAccountId) {
        String passiveId = passiveService.createUserActionPassive(performedByAccountId);
        TaskRelation taskRelation = taskRelationRetrieveService.retrieveTaskRelationEntity(relationId);
        taskRelation.setPassiveId(passiveId);
        return taskRelationRepository.save(taskRelation);
    }

    private void initializeTaskRelationInitializedActivity(TaskRelation relation, String performedByAccountId) {
        TaskDto taskDto = taskRetrieveService.retrievePlain(relation.getTaskId());
        WorkspaceActivityCreateVo vo = WorkspaceActivityCreateVo
                .builder()
                .workspaceId(taskDto.getWorkspaceId())
                .teamId(taskDto.getTeamId())
                .taskId(relation.getTaskId())
                .performedBy(performedByAccountId)
                .relatedObjectId(relation.getTaskRelationId())
                .type(RELATION_REMOVED)
                .build();
        WorkspaceActivityCreateVo vo2 = WorkspaceActivityCreateVo
                .builder()
                .workspaceId(taskDto.getWorkspaceId())
                .teamId(taskDto.getTeamId())
                .taskId(relation.getRelatedTaskId())
                .performedBy(performedByAccountId)
                .relatedObjectId(relation.getTaskRelationId())
                .type(RELATION_REMOVED)
                .build();
        workspaceActivityService.createWorkspaceActivity(vo);
        workspaceActivityService.createWorkspaceActivity(vo2);
    }
}
