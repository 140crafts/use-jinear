package co.jinear.core.repository.notetag;

import co.jinear.core.model.entity.note.NoteTagAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteTagAssignmentRepository extends JpaRepository<NoteTagAssignment, String> {

    List<NoteTagAssignment> findAllByNoteIdAndPassiveIdIsNull(String noteId);

    Optional<NoteTagAssignment> findFirstByNoteIdAndNoteTagIdAndPassiveIdIsNull(String noteId, String noteTagId);

    boolean existsByNoteIdAndNoteTagIdAndPassiveIdIsNull(String noteId, String noteTagId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update NoteTagAssignment assignment
                set assignment.passiveId = :passiveId
                    where
                        assignment.noteId = :noteId and
                        assignment.passiveId is null
                """)
    void passivizeAllByNoteId(@Param("noteId") String noteId, @Param("passiveId") String passiveId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update NoteTagAssignment assignment
                set assignment.passiveId = :passiveId
                    where
                        assignment.noteTagId = :noteTagId and
                        assignment.passiveId is null
                """)
    void passivizeAllByNoteTagId(@Param("noteTagId") String noteTagId, @Param("passiveId") String passiveId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update NoteTagAssignment assignment
                set assignment.passiveId = :passiveId
                    where
                        assignment.passiveId is null and
                        assignment.noteId in (
                            select note.noteId from Note note
                                where note.notebookId = :notebookId
                        )
                """)
    void passivizeAllByNotebookId(@Param("notebookId") String notebookId, @Param("passiveId") String passiveId);
}
