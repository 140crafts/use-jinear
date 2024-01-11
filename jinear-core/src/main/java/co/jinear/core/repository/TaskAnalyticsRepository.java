package co.jinear.core.repository;

import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.vo.task.TaskCountByWorkflowStatusGroupVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface TaskAnalyticsRepository extends JpaRepository<Task, String> {

    Long countAllByTopicId(String topicId);

    Long countAllByTeamId(String teamId);

    @Query("""
                 select count(t)
                 from Task t where
                 t.workspaceId = :workspaceId and
                 t.teamId = :teamId and
                 t.workflowStatus.workflowStateGroup in (:workflowStatuses) and
                 (t.assignedDate < :beforeDate or t.dueDate < :beforeDate) and
                 t.passiveId is null
            """)
    Long countTasksBeforeDateWithStatus(@Param("workspaceId") String workspaceId,
                                        @Param("teamId") String teamId,
                                        @Param("workflowStatuses") List<TeamWorkflowStateGroup> teamWorkflowStateGroup,
                                        @Param("beforeDate") ZonedDateTime beforeDate);

    @Query("""
                 select count(t)
                 from Task t where
                 t.workspaceId = :workspaceId and
                 t.teamId = :teamId and
                 t.workflowStatus.workflowStateGroup in (:workflowStatuses) and
                    ((:relativeDateStart < t.assignedDate and t.assignedDate < :relativeDateEnd) or
                    (:relativeDateStart < t.dueDate and t.dueDate < :relativeDateEnd)) and
                 t.passiveId is null
            """)
    Long countTasksWithinDateWithStatus(@Param("workspaceId") String workspaceId,
                                        @Param("teamId") String teamId,
                                        @Param("workflowStatuses") List<TeamWorkflowStateGroup> teamWorkflowStateGroup,
                                        @Param("relativeDateStart") ZonedDateTime relativeDateStart,
                                        @Param("relativeDateEnd") ZonedDateTime relativeDateEnd);

    Long countAllByWorkspaceIdAndTeamIdAndWorkflowStatus_WorkflowStateGroupIsInAndPassiveIdIsNull(String workspaceId, String teamId, List<TeamWorkflowStateGroup> workflowStateGroups);

    @Query("""
            select new co.jinear.core.model.vo.task.TaskCountByWorkflowStatusGroupVo(t.workflowStatus.workflowStateGroup, count(t))
            from Task t
            where
                 t.workspaceId = :workspaceId and
                 t.teamId = :teamId and
                 t.passiveId is null
                 group by t.workflowStatusId, t.workflowStatus.workflowStateGroup
            """)
    List<TaskCountByWorkflowStatusGroupVo> countTasksByWorkflowStatusGroups(String workspaceId, String teamId);
}
