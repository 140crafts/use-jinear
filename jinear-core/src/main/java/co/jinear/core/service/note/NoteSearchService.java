package co.jinear.core.service.note;

import co.jinear.core.converter.note.NoteDtoConverter;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.entity.note.Note;
import co.jinear.core.repository.note.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteSearchService {

    private static final int PAGE_SIZE = 250;

    private final NoteRepository noteRepository;
    private final NoteDtoConverter noteDtoConverter;

    public Page<NoteDto> search(String notebookId, String parentNoteId, int page) {
        log.info("Search notes has started. notebookId: {}, parentNoteId: {}, page: {}", notebookId, parentNoteId, page);
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<Note> result = Objects.isNull(parentNoteId) ?
                noteRepository.findAllByNotebookIdAndParentNoteIdIsNullAndPassiveIdIsNullOrderByCreatedDateDesc(notebookId, pageRequest) :
                noteRepository.findAllByNotebookIdAndParentNoteIdAndPassiveIdIsNullOrderByCreatedDateDesc(notebookId, parentNoteId, pageRequest);
        return result.map(noteDtoConverter::convert);
    }
}
