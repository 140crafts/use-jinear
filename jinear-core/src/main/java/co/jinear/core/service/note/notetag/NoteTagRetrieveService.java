package co.jinear.core.service.note.notetag;

import co.jinear.core.converter.notetag.NoteTagDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.notetag.NoteTagDto;
import co.jinear.core.model.entity.note.NoteTag;
import co.jinear.core.repository.notetag.NoteTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteTagRetrieveService {

    private final NoteTagRepository noteTagRepository;
    private final NoteTagDtoConverter noteTagDtoConverter;

    public NoteTagDto retrieve(String noteTagId) {
        log.info("Retrieve note tag has started. noteTagId: {}", noteTagId);
        return noteTagRepository.findByNoteTagIdAndPassiveIdIsNull(noteTagId)
                .map(noteTagDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public NoteTag retrieveEntity(String noteTagId) {
        return noteTagRepository.findByNoteTagIdAndPassiveIdIsNull(noteTagId)
                .orElseThrow(NotFoundException::new);
    }

    public boolean existsByNoteTagIdAndNotebookId(String noteTagId, String notebookId) {
        return noteTagRepository.existsByNoteTagIdAndNotebookIdAndPassiveIdIsNull(noteTagId, notebookId);
    }

    public List<NoteTagDto> retrieveNotebookTags(String notebookId) {
        log.info("Retrieve notebook tags has started. notebookId: {}", notebookId);
        return noteTagRepository.findAllByNotebookIdAndPassiveIdIsNullOrderByCreatedDateDesc(notebookId)
                .stream()
                .map(noteTagDtoConverter::convert)
                .toList();
    }
}
