package co.jinear.core.repository.note;

import co.jinear.core.model.entity.note.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, String> {

    Optional<Note> findByNoteIdAndPassiveIdIsNull(String noteId);

    boolean existsByNoteIdAndNotebookIdAndPassiveIdIsNull(String noteId, String notebookId);

    List<Note> findAllByParentNoteIdAndPassiveIdIsNull(String parentNoteId);

    @EntityGraph(attributePaths = {"notebook", "owner", "richText", "workspace", "noteTagAssignments", "noteTagAssignments.noteTag"})
    List<Note> findAllByNoteIdInAndPassiveIdIsNull(List<String> noteIds);

    // note_id is char(26); the varchar cast makes the native scalar come back as a String.
    @Query(value = """
            select cast(n.note_id as varchar) as note_id
            from note n
                     left join notebook nb on nb.notebook_id = n.notebook_id and nb.passive_id is null
            where n.passive_id is null
              and n.workspace_id = :workspaceId
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
            order by n.idate desc, n.note_id desc
            """,
            countQuery = """
                    select count(*)
                    from note n
                             left join notebook nb on nb.notebook_id = n.notebook_id and nb.passive_id is null
                    where n.passive_id is null
                      and n.workspace_id = :workspaceId
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
    Page<String> findAccessibleNoteIds(@Param("workspaceId") String workspaceId,
                                       @Param("notebookId") String notebookId,
                                       @Param("parentNoteId") String parentNoteId,
                                       @Param("accountId") String accountId,
                                       @Param("workspaceAdmin") boolean workspaceAdmin,
                                       Pageable pageable);

    @Query(value = """
            select cast(link.note_id as varchar) as note_id
            from mv_note_task_link link
                     inner join note n on n.note_id = link.note_id and n.passive_id is null
            where link.task_id = :taskId
              and n.workspace_id = :workspaceId
            order by coalesce(n.udate, n.idate) desc, n.note_id desc
            limit :limit
            """, nativeQuery = true)
    List<String> findNoteIdsLinkingTask(@Param("taskId") String taskId,
                                        @Param("workspaceId") String workspaceId,
                                        @Param("limit") int limit);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Note note
                set note.passiveId = :passiveId
                    where
                        note.notebookId = :notebookId and
                        note.passiveId is null
                """)
    void passivizeAllByNotebookId(@Param("notebookId") String notebookId, @Param("passiveId") String passiveId);

    @Query(value = """
            WITH RECURSIVE note_path AS (
                SELECT
                    note_id,
                    parent_note_id,
                    title,
                    0 as depth
                FROM note
                WHERE note_id = :noteId
                  AND passive_id IS NULL

                UNION ALL

                SELECT
                    n.note_id,
                    n.parent_note_id,
                    n.title,
                    np.depth + 1 as depth
                FROM note n
                INNER JOIN note_path np ON n.note_id = np.parent_note_id
                WHERE n.passive_id IS NULL
            )
            SELECT * FROM note_path
            ORDER BY depth DESC
            """, nativeQuery = true)
    List<Object[]> findNotePath(@Param("noteId") String noteId);
}
