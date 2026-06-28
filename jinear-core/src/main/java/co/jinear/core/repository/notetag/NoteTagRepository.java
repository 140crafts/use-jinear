package co.jinear.core.repository.notetag;

import co.jinear.core.model.entity.note.NoteTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteTagRepository extends JpaRepository<NoteTag, String> {

    Optional<NoteTag> findByNoteTagIdAndPassiveIdIsNull(String noteTagId);

    boolean existsByNoteTagIdAndNotebookIdAndPassiveIdIsNull(String noteTagId, String notebookId);

    List<NoteTag> findAllByNotebookIdAndPassiveIdIsNullOrderByCreatedDateDesc(String notebookId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update NoteTag noteTag
                set noteTag.passiveId = :passiveId
                    where
                        noteTag.notebookId = :notebookId and
                        noteTag.passiveId is null
                """)
    void passivizeAllByNotebookId(@Param("notebookId") String notebookId, @Param("passiveId") String passiveId);
}
