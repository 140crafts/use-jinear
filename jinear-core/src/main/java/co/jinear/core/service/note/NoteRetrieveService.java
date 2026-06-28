package co.jinear.core.service.note;

import co.jinear.core.converter.note.NoteDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.note.NotePathDto;
import co.jinear.core.model.dto.note.NotePathItemDto;
import co.jinear.core.model.dto.note.PathAwareNoteDto;
import co.jinear.core.model.entity.note.Note;
import co.jinear.core.repository.note.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteRetrieveService {

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

    public boolean existsByNoteIdAndNotebookId(String noteId, String notebookId) {
        return noteRepository.existsByNoteIdAndNotebookIdAndPassiveIdIsNull(noteId, notebookId);
    }

    public PathAwareNoteDto retrievePathAware(String noteId) {
        log.info("Retrieve path aware note has started. noteId: {}", noteId);
        NoteDto noteDto = retrieve(noteId);
        NotePathDto notePath = getNotePath(noteId);
        return noteDtoConverter.convert(noteDto, notePath);
    }

    private NotePathDto getNotePath(String noteId) {
        log.info("Get note path has started. noteId: {}", noteId);
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
}
