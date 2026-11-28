package co.jinear.core.service.note;

import co.jinear.core.converter.note.NoteDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.note.NotePathDto;
import co.jinear.core.model.dto.note.NotePathItemDto;
import co.jinear.core.model.entity.note.Note;
import co.jinear.core.repository.note.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteRetrieveService {

    private static final int TASK_LINK_CANDIDATE_LIMIT = 100;

    private final NoteRepository noteRepository;
    private final NoteDtoConverter noteDtoConverter;

    public NoteDto retrieve(String noteId) {
        log.info("Retrieve note has started. noteId: {}", noteId);
        return noteRepository.findByNoteIdAndPassiveIdIsNull(noteId)
                .map(noteDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public Note retrieveEntity(String noteId) {
        return noteRepository.findByNoteIdAndPassiveIdIsNull(noteId)
                .orElseThrow(NotFoundException::new);
    }

    public List<NoteDto> retrieveAll(List<String> noteIds) {
        if (noteIds.isEmpty()) {
            return List.of();
        }
        return noteRepository.findAllByNoteIdInAndPassiveIdIsNull(noteIds)
                .stream()
                .map(noteDtoConverter::convert)
                .toList();
    }

    public List<NoteDto> retrieveAllLinkingTask(String taskId, String workspaceId) {
        log.info("Retrieve all notes linking task has started. taskId: {}, workspaceId: {}", taskId, workspaceId);
        List<String> noteIds = noteRepository.findNoteIdsLinkingTask(taskId, workspaceId, TASK_LINK_CANDIDATE_LIMIT);
        List<NoteDto> notes = retrieveAll(noteIds);
        return sortByIdOrder(notes, noteIds);
    }

    public boolean existsByNoteIdAndNotebookId(String noteId, String notebookId) {
        return noteRepository.existsByNoteIdAndNotebookIdAndPassiveIdIsNull(noteId, notebookId);
    }

    public NotePathDto retrieveNotePath(String noteId) {
        log.info("Retrieve note path has started. noteId: {}", noteId);
        List<Object[]> pathData = noteRepository.findNotePath(noteId);
        List<NotePathItemDto> path = pathData.stream()
                .map(row -> NotePathItemDto.builder()
                        .noteId((String) row[0])
                        .parentNoteId((String) row[1])
                        .title((String) row[2])
                        .build())
                .toList();

        return NotePathDto.builder()
                .noteId(noteId)
                .path(path)
                .fullPath(path.stream()
                        .map(NotePathItemDto::getTitle)
                        .collect(Collectors.joining(" / ")))
                .build();
    }

    private List<NoteDto> sortByIdOrder(List<NoteDto> notes, List<String> noteIds) {
        return notes.stream()
                .sorted(Comparator.comparingInt(note -> noteIds.indexOf(note.getNoteId())))
                .toList();
    }
}
