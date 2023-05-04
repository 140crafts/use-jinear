package co.jinear.core.repository;

import co.jinear.core.model.entity.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String> {

    Optional<Task> findByTaskIdAndPassiveIdIsNull(String taskId);

    Long countAllByTopicId(String topicId);

    Long countAllByTeamId(String teamId);

    Page<Task> findAllByWorkspaceIdAndTeamIdAndPassiveIdIsNullOrderByCreatedDateDesc(String workspaceId, String teamId, Pageable pageable);

    Page<Task> findAllByWorkspaceIdAndTeamIdAndWorkflowStatusIdAndPassiveIdIsNullOrderByCreatedDateDesc(String workspaceId, String teamId, String workflowStatusId, Pageable pageable);

    Page<Task> findAllByWorkspaceIdAndTeamIdAndTopicIdAndPassiveIdIsNullOrderByCreatedDateDesc(String workspaceId, String teamId, String topicId, Pageable pageable);

    Page<Task> findAllByWorkspaceIdAndTeamIdAndAssignedToAndPassiveIdIsNullOrderByCreatedDateDesc(String workspaceId, String teamId, String assignedTo, Pageable pageable);

    Page<Task> findAllByWorkspaceIdAndAssignedToAndPassiveIdIsNullOrderByCreatedDateDesc(String workspaceId, String assignedTo, Pageable pageable);

    Optional<Task> findByWorkspaceIdAndTeamIdAndTeamTagNoAndPassiveIdIsNull(String workspaceId, String teamId, Integer teamTagNo);
}
