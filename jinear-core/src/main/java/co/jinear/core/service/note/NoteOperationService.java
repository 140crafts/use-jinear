package co.jinear.core.service.note;

import co.jinear.core.converter.note.NoteEntityConverter;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.entity.note.Note;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.vo.note.NoteInitializeVo;
import co.jinear.core.model.vo.note.NoteUpdateVo;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.repository.note.NoteRepository;
import co.jinear.core.service.note.notetag.NoteTagAssignmentOperationService;
import co.jinear.core.service.richtext.RichTextInitializeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteOperationService {

    private final NoteRepository noteRepository;
    private final NoteEntityConverter noteEntityConverter;
    private final NoteRetrieveService noteRetrieveService;
    private final RichTextInitializeService richTextInitializeService;
    private final NoteTagAssignmentOperationService noteTagAssignmentOperationService;

    @Transactional
    public String initialize(NoteInitializeVo noteInitializeVo) {
        log.info("Initialize note has started. noteInitializeVo: {}", noteInitializeVo);
        validateParentNoteIsWithinSameNotebook(noteInitializeVo.getParentNoteId(), noteInitializeVo.getNotebookId());
        Note note = noteEntityConverter.convert(noteInitializeVo);
        Note saved = noteRepository.save(note);
        String richTextId = initializeRichTextBody(saved.getNoteId(), noteInitializeVo.getBodyState());
        saved.setRichTextId(richTextId);
        noteRepository.save(saved);
        return saved.getNoteId();
    }

    public void update(NoteUpdateVo noteUpdateVo) {
        log.info("Update note has started. noteUpdateVo: {}", noteUpdateVo);
        Note note = noteRetrieveService.retrieveEntity(noteUpdateVo.getNoteId());
        note.setTitle(noteUpdateVo.getTitle());
        noteRepository.save(note);
    }

    public void moveTo(String noteId, String parentNoteId) {
        log.info("Move note has started. noteId: {}, parentNoteId: {}", noteId, parentNoteId);
        Note note = noteRetrieveService.retrieveEntity(noteId);
        if (Objects.nonNull(parentNoteId)) {
            validateParentNoteIsWithinSameNotebook(parentNoteId, note.getNotebookId());
            validateNotCreatingCycle(noteId, parentNoteId);
        }
        note.setParentNoteId(parentNoteId);
        noteRepository.save(note);
    }

    @Transactional
    public void delete(String noteId, String passiveId) {
        log.info("Delete note has started. noteId: {}, passiveId: {}", noteId, passiveId);
        Deque<String> queue = new ArrayDeque<>();
        queue.add(noteId);
        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            noteRepository.findByNoteIdAndPassiveIdIsNull(currentId)
                    .ifPresent(note -> {
                        note.setPassiveId(passiveId);
                        noteRepository.save(note);
                        noteTagAssignmentOperationService.removeAllByNote(note.getNoteId(), passiveId);
                        noteRepository.findAllByParentNoteIdAndPassiveIdIsNull(note.getNoteId())
                                .forEach(child -> queue.add(child.getNoteId()));
                    });
        }
    }

    @Transactional
    public void deleteAllByNotebook(String notebookId, String passiveId) {
        log.info("Delete all notes by notebook has started. notebookId: {}, passiveId: {}", notebookId, passiveId);
        noteTagAssignmentOperationService.removeAllByNotebook(notebookId, passiveId);
        noteRepository.passivizeAllByNotebookId(notebookId, passiveId);
    }

    private String initializeRichTextBody(String noteId, String bodyState) {
        InitializeRichTextVo initializeRichTextVo = new InitializeRichTextVo();
        initializeRichTextVo.setRelatedObjectId(noteId);
        initializeRichTextVo.setType(RichTextType.NOTE);
        initializeRichTextVo.setValue("");
        initializeRichTextVo.setCrdtState(bodyState);
        RichTextDto richTextDto = richTextInitializeService.initializeRichText(initializeRichTextVo);
        return richTextDto.getRichTextId();
    }

    private void validateParentNoteIsWithinSameNotebook(String parentNoteId, String notebookId) {
        if (Objects.nonNull(parentNoteId) && !noteRetrieveService.existsByNoteIdAndNotebookId(parentNoteId, notebookId)) {
            log.info("Parent note is not within the same notebook. parentNoteId: {}, notebookId: {}", parentNoteId, notebookId);
            throw new NotValidException();
        }
    }

    private void validateNotCreatingCycle(String noteId, String parentNoteId) {
        if (noteId.equals(parentNoteId)) {
            throw new NotValidException();
        }
        List<Object[]> parentPath = noteRepository.findNotePath(parentNoteId);
        boolean noteIsAncestorOfParent = parentPath.stream()
                .anyMatch(row -> noteId.equals(row[0]));
        if (noteIsAncestorOfParent) {
            log.info("Move would create a cycle. noteId: {}, parentNoteId: {}", noteId, parentNoteId);
            throw new NotValidException();
        }
    }
}
