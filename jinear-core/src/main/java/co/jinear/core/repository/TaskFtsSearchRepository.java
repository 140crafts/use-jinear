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
                 , ts_rank_cd(fts, to_tsquery('simple',
                       (select string_agg(tok || ':*', ' & ')
                        from unnest(regexp_split_to_array(trim(regexp_replace(:q, '[^\\w\\s]', ' ', 'g')), '\\s+')) tok
                        where tok <> ''))) as search_ranking
            from mv_task_fts ft
            where fts @@ to_tsquery('simple',
                      (select string_agg(tok || ':*', ' & ')
                       from unnest(regexp_split_to_array(trim(regexp_replace(:q, '[^\\w\\s]', ' ', 'g')), '\\s+')) tok
                       where tok <> ''))
              and ft.workspace_id = :workspaceId
              and (
                    (ft.team_task_visibility = 0 and ft.team_id in :visibleToAllTeamIds) or
                    (ft.team_task_visibility = 1 and (ft.owner_id = :ownerId or ft.assigned_to = :assignedTo) and ft.team_id in :ownerOrAssigneeTeamIds)
                  )
              and passive_id is null
            order by search_ranking desc
            """,
            countQuery = """
                    select count(*)
                    from mv_task_fts ft
                    where fts @@ to_tsquery('simple',
                              (select string_agg(tok || ':*', ' & ')
                               from unnest(regexp_split_to_array(trim(regexp_replace(:q, '[^\\w\\s]', ' ', 'g')), '\\s+')) tok
                               where tok <> ''))
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
}
