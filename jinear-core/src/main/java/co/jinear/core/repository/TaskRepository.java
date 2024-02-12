package co.jinear.core.repository;

import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String> {

    Optional<Task> findByTaskIdAndPassiveIdIsNull(String taskId);

    Optional<Task> findByWorkspaceIdAndTeamIdAndTeamTagNoAndPassiveIdIsNull(String workspaceId, String teamId, Integer teamTagNo);

    Page<Task> findAllByWorkspaceIdAndTeamIdAndWorkflowStatus_WorkflowStateGroupIsInAndPassiveIdIsNullOrderByCreatedDateAsc(String workspaceId, String teamId, List<TeamWorkflowStateGroup> workflowStateGroups, Pageable pageable);


}