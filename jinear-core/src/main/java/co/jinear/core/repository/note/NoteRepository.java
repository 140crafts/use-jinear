package co.jinear.core.repository.note;

import co.jinear.core.model.entity.note.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, String> {

    Optional<Note> findByNoteIdAndPassiveIdIsNull(String noteId);

    boolean existsByNoteIdAndNotebookIdAndPassiveIdIsNull(String noteId, String notebookId);

    Page<Note> findAllByNotebookIdAndParentNoteIdIsNullAndPassiveIdIsNullOrderByCreatedDateDesc(String notebookId, Pageable pageable);

    Page<Note> findAllByNotebookIdAndParentNoteIdAndPassiveIdIsNullOrderByCreatedDateDesc(String notebookId, String parentNoteId, Pageable pageable);

    List<Note> findAllByParentNoteIdAndPassiveIdIsNull(String parentNoteId);

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
