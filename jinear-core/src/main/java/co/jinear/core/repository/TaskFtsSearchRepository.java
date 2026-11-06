package co.jinear.core.repository;

import co.jinear.core.model.entity.task.TaskFts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskFtsSearchRepository extends JpaRepository<TaskFts, String> {

    @Query(value = """
            select *
                 , ts_rank_cd(fts, jinear_fts_prefix_query(:q)) as search_ranking
            from mv_task_fts ft
            where fts @@ jinear_fts_prefix_query(:q)
              and ft.workspace_id = :workspaceId
              and (
                    ft.team_id in :visibleToAllTeamIds or
                    ((ft.owner_id = :ownerId or ft.assigned_to = :assignedTo) and ft.team_id in :ownerOrAssigneeTeamIds)
                  )
              and passive_id is null
            order by search_ranking desc
            """,
            countQuery = """
                    select count(*)
                    from mv_task_fts ft
                    where fts @@ jinear_fts_prefix_query(:q)
                      and ft.workspace_id = :workspaceId
                      and (
                            ft.team_id in :visibleToAllTeamIds or
                            ((ft.owner_id = :ownerId or ft.assigned_to = :assignedTo) and ft.team_id in :ownerOrAssigneeTeamIds)
                          )
                      and passive_id is null
                    """,
            nativeQuery = true)
    Page<TaskFts> search(@Param("q") String query,
                         @Param("workspaceId") String workspaceId,
                         @Param("visibleToAllTeamIds") List<String> visibleToAllTeamIds,
                         @Param("ownerOrAssigneeTeamIds") List<String> ownerOrAssigneeTeamIds,
                         @Param("assignedTo") String assignedTo,
                         @Param("ownerId") String ownerId,
                         Pageable pageable);
}
