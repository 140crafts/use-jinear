package co.jinear.core.repository;

import co.jinear.core.model.entity.task.Task;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskFtsSearchRepository extends JpaRepository<Task, String> {

    @Query(value = """
            select 
                task.task_id,
                task.topic_id,
                task.workspace_id,
                task.team_id,
                task.owner_id,
                task.workflow_status_id,
                task.assigned_to,
                task.assigned_date,
                task.due_date,
                task.has_precise_assigned_date,
                task.has_precise_due_date,
                task.team_tag_no,
                task.topic_tag_no,
                task.title,
                team.tag,
                teamWorkflowStatus.name as workflowStateName,
                teamWorkflowStatus.workflow_state_group as workflowStateGroup
            from task
                inner join team_workflow_status tws on task.workflow_status_id = tws.team_workflow_status_id
                inner join topic topic on task.topic_id = topic.topic_id
                inner join team team on task.team_id = team.team_id
                inner join team_workflow_status teamWorkflowStatus on task.workflow_status_id = teamWorkflowStatus.team_workflow_status_id
                    where match(task.title) against(:title'*' in boolean mode)
                          and task.workspace_id = :workspaceId
                          and task.team_id = :teamId
                          and task.passive_id is null
                          order by task.idate asc
                        """,
            countQuery = """
                    select count(*)
                        from task
                        where match(title) against(:title'*' in boolean mode)
                          and workspace_id = :workspaceId
                          and team_id = :teamId
                          and passive_id is null
                          order by idate asc
                                """,
            nativeQuery = true)
    Page<Tuple> searchAllTasks(@Param("title") String title,
                               @Param("workspaceId") String workspaceId,
                               @Param("teamId") String teamId,
                               Pageable pageable);
}
