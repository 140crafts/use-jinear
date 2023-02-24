package co.jinear.core.service.task.relation;

import co.jinear.core.converter.task.TaskRelationConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.task.TaskRelationDto;
import co.jinear.core.model.entity.task.TaskRelation;
import co.jinear.core.model.enumtype.task.TaskRelationType;
import co.jinear.core.repository.TaskRelationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskRelationRetrieveService {

    private final TaskRelationRepository taskRelationRepository;
    private final TaskRelationConverter taskRelationConverter;

    public TaskRelationDto retrieveTaskRelation(String taskRelationId) {
        log.info("Retrieve task relation has started. taskRelationId: {}", taskRelationId);
        return taskRelationRepository.findByTaskRelationIdAndPassiveIdIsNull(taskRelationId)
                .map(taskRelationConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public TaskRelationDto retrieveTaskRelationInclPassive(String taskRelationId) {
        log.info("Retrieve task relation has started. taskRelationId: {}", taskRelationId);
        return taskRelationRepository.findByTaskRelationId(taskRelationId)
                .map(taskRelationConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public TaskRelation retrieveTaskRelationEntity(String taskRelationId) {
        log.info("Retrieve task relation has started. taskRelationId: {}", taskRelationId);
        return taskRelationRepository.findByTaskRelationIdAndPassiveIdIsNull(taskRelationId)
                .orElseThrow(NotFoundException::new);
    }

    public boolean isRelationExists(String taskId, String relatedTaskId, TaskRelationType relationType) {
        log.info("Checking if relation exists. taskId: {}, relatedTaskId: {}, relationType: {}", taskId, relatedTaskId, relationType);
        boolean isPresent = taskRelationRepository.findByTaskIdAndRelatedTaskIdAndRelationTypeAndPassiveIdIsNull(taskId, relatedTaskId, relationType).isPresent();
        log.info("Relation exists: {}", isPresent);
        return isPresent;
    }
}
