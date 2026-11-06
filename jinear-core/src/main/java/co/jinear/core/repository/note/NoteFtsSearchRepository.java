package co.jinear.core.repository.note;

import co.jinear.core.model.entity.note.NoteFts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteFtsSearchRepository extends JpaRepository<NoteFts, String> {

    @Query(value = """
            select nf.*
            from mv_note_fts nf
                     inner join note n on n.note_id = nf.note_id and n.passive_id is null
                     left join notebook nb on nb.notebook_id = n.notebook_id and nb.passive_id is null
            where nf.fts @@ jinear_fts_prefix_query(:q)
              and nf.workspace_id = :workspaceId
              and (cast(:notebookId as varchar) is null or n.notebook_id = cast(:notebookId as varchar))
              and (cast(:parentNoteId as varchar) is null or n.parent_note_id = cast(:parentNoteId as varchar))
              and (
                    n.owner_id = :accountId or
                    (nb.notebook_id is not null and (
                        :workspaceAdmin = true or
                        nb.owner_id = :accountId or
                        nb.visibility = 2 or
                        (nb.visibility = 1 and exists (select 1
                                                       from notebook_member nm
                                                       where nm.notebook_id = nb.notebook_id
                                                         and nm.account_id = :accountId
                                                         and nm.passive_id is null))
                    ))
                  )
            order by ts_rank_cd(nf.fts, jinear_fts_prefix_query(:q)) desc,
                     nf.note_id desc
            """,
            countQuery = """
                    select count(*)
                    from mv_note_fts nf
                             inner join note n on n.note_id = nf.note_id and n.passive_id is null
                             left join notebook nb on nb.notebook_id = n.notebook_id and nb.passive_id is null
                    where nf.fts @@ jinear_fts_prefix_query(:q)
                      and nf.workspace_id = :workspaceId
                      and (cast(:notebookId as varchar) is null or n.notebook_id = cast(:notebookId as varchar))
                      and (cast(:parentNoteId as varchar) is null or n.parent_note_id = cast(:parentNoteId as varchar))
                      and (
                            n.owner_id = :accountId or
                            (nb.notebook_id is not null and (
                                :workspaceAdmin = true or
                                nb.owner_id = :accountId or
                                nb.visibility = 2 or
                                (nb.visibility = 1 and exists (select 1
                                                               from notebook_member nm
                                                               where nm.notebook_id = nb.notebook_id
                                                                 and nm.account_id = :accountId
                                                                 and nm.passive_id is null))
                            ))
                          )
                    """,
            nativeQuery = true)
    Page<NoteFts> search(@Param("q") String query,
                         @Param("workspaceId") String workspaceId,
                         @Param("notebookId") String notebookId,
                         @Param("parentNoteId") String parentNoteId,
                         @Param("accountId") String accountId,
                         @Param("workspaceAdmin") boolean workspaceAdmin,
                         Pageable pageable);
}
