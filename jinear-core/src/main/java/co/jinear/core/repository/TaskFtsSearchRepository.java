package co.jinear.core.repository;

import co.jinear.core.model.entity.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskFtsSearchRepository extends JpaRepository<Task, String> {

    @Query(value = """
            select *
            from task
                where match(title) against(:title'*' in boolean mode)
                  and workspace_id = :workspaceId
                  and team_id = :teamId
                  and passive_id is null
                  order by idate asc
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
    Page<Task> searchAllTasks(@Param("title") String title,
                              @Param("workspaceId") String workspaceId,
                              @Param("teamId") String teamId,
                              Pageable pageable);
}
