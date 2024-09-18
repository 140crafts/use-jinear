package co.jinear.core.repository;

import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String> {

    Optional<Task> findByTaskIdAndPassiveIdIsNull(String taskId);

    Optional<Task> findByWorkspaceIdAndTeamIdAndTeamTagNoAndPassiveIdIsNull(String workspaceId, String teamId, Integer teamTagNo);

    Page<Task> findAllByWorkspaceIdAndTeamIdAndWorkflowStatus_WorkflowStateGroupIsInAndPassiveIdIsNullOrderByCreatedDateAsc(String workspaceId, String teamId, List<TeamWorkflowStateGroup> workflowStateGroups, Pageable pageable);

    boolean existsByTeamIdIsInAndProjectIdAndPassiveIdIsNull(List<String> teamIds, String projectId);

    boolean existsByMilestoneIdAndPassiveIdIsNull(String milestoneId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Task t
                set t.milestoneId = null, t.projectId = null, t.lastUpdatedDate = current_timestamp()
                    where
                        t.milestoneId = :milestoneId and
                        t.passiveId is null
                """)
    void updateAllMilestoneIdsAndProjectIdsAsNullWithMilestoneId(@Param("milestoneId") String milestoneId);
}