package co.jinear.core.repository;

import co.jinear.core.model.entity.task.TaskRelation;
import co.jinear.core.model.enumtype.task.TaskRelationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRelationRepository extends JpaRepository<TaskRelation, String> {

    Optional<TaskRelation> findByTaskRelationId(String taskRelationId);

    Optional<TaskRelation> findByTaskRelationIdAndPassiveIdIsNull(String taskRelationId);

    Optional<TaskRelation> findByTaskIdAndRelatedTaskIdAndRelationTypeAndPassiveIdIsNull(String taskId, String relatedTaskId, TaskRelationType relationType);
}
