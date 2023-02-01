package co.jinear.core.repository;

import co.jinear.core.model.entity.task.TaskRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRelationRepository extends JpaRepository<TaskRelation, String> {
    Optional<TaskRelation> findByTaskRelationIdAndPassiveIdIsNull(String taskRelationId);
}
