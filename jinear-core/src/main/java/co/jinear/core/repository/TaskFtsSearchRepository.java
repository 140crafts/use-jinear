package co.jinear.core.repository;

import co.jinear.core.model.entity.task.TaskFts;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskFtsSearchRepository extends JpaRepository<TaskFts, String> {

    @Query(value = """
            select *
                 , greatest(ts_rank_cd(fts, to_tsquery(concat(regexp_replace(trim(:q), '\\W+', ':* & ', 'gm'), ':*'))),
                            ts_rank_cd(fts,
                                       to_tsquery('simple',
                                                  concat(regexp_replace(trim(:q), '\\W+', ':* & ', 'gm'), ':*')))) as search_ranking
            from mv_task_fts ft
            where (
                        fts @@ to_tsquery(concat(regexp_replace(trim(:q), '\\W+', ':* & ', 'gm'), ':*')) or
                        fts @@ to_tsquery('simple', concat(regexp_replace(trim(:q), '\\W+', ':* & ', 'gm'), ':*'))
                  )
              and ft.workspace_id = :workspaceId
              and (
                    (ft.team_task_visibility = 0 and ft.team_id in :visibleToAllTeamIds) or
                    (ft.team_task_visibility = 1 and (ft.owner_id = :ownerId or ft.assigned_to = :assignedTo) and ft.team_id in :ownerOrAssigneeTeamIds)
                  )
              and passive_id is null
            order by search_ranking desc;
                        """,
            countQuery = """
                            select count(*)
                            from mv_task_fts ft
                            where (
                            fts @@ to_tsquery(concat(regexp_replace(trim(:q), '\\W+', ':* & ', 'gm'), ':*')) or
                            fts @@ to_tsquery('simple', concat(regexp_replace(trim(:q), '\\W+', ':* & ', 'gm'), ':*'))
                                  )
                        and ft.workspace_id = :workspaceId
                        and (
                        (ft.team_task_visibility = 0 and ft.team_id in :visibleToAllTeamIds) or
                        (ft.team_task_visibility = 1 and (ft.owner_id = :ownerId or ft.assigned_to = :assignedTo) and ft.team_id in :ownerOrAssigneeTeamIds)
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

    @Modifying
    @Transactional
    @Query(value = "refresh materialized view concurrently mv_task_fts;", nativeQuery = true)
    void refreshFtsTasksView();
}
