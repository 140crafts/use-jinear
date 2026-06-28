package co.jinear.core.service.note.notetag;

import co.jinear.core.model.entity.note.NoteTagAssignment;
import co.jinear.core.repository.notetag.NoteTagAssignmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteTagAssignmentOperationService {

    private final NoteTagAssignmentRepository noteTagAssignmentRepository;

    public void assign(String noteId, String noteTagId) {
        log.info("Assign note tag has started. noteId: {}, noteTagId: {}", noteId, noteTagId);
        if (noteTagAssignmentRepository.existsByNoteIdAndNoteTagIdAndPassiveIdIsNull(noteId, noteTagId)) {
            log.info("Note tag is already assigned. Skipping. noteId: {}, noteTagId: {}", noteId, noteTagId);
            return;
        }
        NoteTagAssignment assignment = new NoteTagAssignment();
        assignment.setNoteId(noteId);
        assignment.setNoteTagId(noteTagId);
        noteTagAssignmentRepository.save(assignment);
    }

    public void unassign(String noteId, String noteTagId, String passiveId) {
        log.info("Unassign note tag has started. noteId: {}, noteTagId: {}, passiveId: {}", noteId, noteTagId, passiveId);
        noteTagAssignmentRepository.findFirstByNoteIdAndNoteTagIdAndPassiveIdIsNull(noteId, noteTagId)
                .ifPresent(assignment -> {
                    assignment.setPassiveId(passiveId);
                    noteTagAssignmentRepository.save(assignment);
                });
    }

    @Transactional
    public void removeAllByNote(String noteId, String passiveId) {
        log.info("Remove all tag assignments by note has started. noteId: {}, passiveId: {}", noteId, passiveId);
        noteTagAssignmentRepository.passivizeAllByNoteId(noteId, passiveId);
    }

    @Transactional
    public void removeAllByTag(String noteTagId, String passiveId) {
        log.info("Remove all tag assignments by tag has started. noteTagId: {}, passiveId: {}", noteTagId, passiveId);
        noteTagAssignmentRepository.passivizeAllByNoteTagId(noteTagId, passiveId);
    }

    @Transactional
    public void removeAllByNotebook(String notebookId, String passiveId) {
        log.info("Remove all tag assignments by notebook has started. notebookId: {}, passiveId: {}", notebookId, passiveId);
        noteTagAssignmentRepository.passivizeAllByNotebookId(notebookId, passiveId);
    }
}
