package co.jinear.core.manager.note;

import co.jinear.core.converter.note.NoteDtoConverter;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.note.NoteHierarchyDto;
import co.jinear.core.model.dto.note.NotePathDto;
import co.jinear.core.model.dto.note.PathAwareNoteDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.response.note.NotePathAwareResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.note.NoteRetrieveService;
import co.jinear.core.service.note.NoteSearchService;
import co.jinear.core.service.note.notebook.NotebookRetrieveService;
import co.jinear.core.validator.notebook.NotebookAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteListingManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final NotebookAccessValidator notebookAccessValidator;
    private final NotebookRetrieveService notebookRetrieveService;
    private final NoteRetrieveService noteRetrieveService;
    private final NoteSearchService noteSearchService;
    private final NoteDtoConverter noteDtoConverter;

    public NotePathAwareResponse list(String notebookId, String noteId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NotebookDto notebookDto = notebookRetrieveService.retrieve(notebookId);
        validateNotebookAccess(currentAccountId, notebookDto);
        log.info("List notes has started. currentAccountId: {}, notebookId: {}, noteId: {}", currentAccountId, notebookId, noteId);
        PathAwareNoteDto pathAwareNoteDto = Optional.ofNullable(noteId).map(noteRetrieveService::retrievePathAware).orElse(null);
        Page<NoteDto> childNotes = noteSearchService.search(notebookId, noteId, page);
        return mapResponse(pathAwareNoteDto, childNotes);
    }

    private void validateNotebookAccess(String currentAccountId, NotebookDto notebookDto) {
        workspaceValidator.validateHasAccess(currentAccountId, notebookDto.getWorkspaceId());
        notebookAccessValidator.validateHasAccess(currentAccountId, notebookDto);
    }

    private NotePathAwareResponse mapResponse(PathAwareNoteDto pathAwareNoteDto, Page<NoteDto> childNotes) {
        NotePathDto parentPath = Optional.ofNullable(pathAwareNoteDto)
                .map(PathAwareNoteDto::getNotePath)
                .orElse(null);
        Page<PathAwareNoteDto> pathAwareChildren = childNotes.map(child -> noteDtoConverter.convertChildWithPath(child, parentPath));

        NoteHierarchyDto noteHierarchyDto = new NoteHierarchyDto();
        noteHierarchyDto.setNote(pathAwareNoteDto);
        noteHierarchyDto.setChildren(new PageDto<>(pathAwareChildren));

        NotePathAwareResponse notePathAwareResponse = new NotePathAwareResponse();
        notePathAwareResponse.setNoteHierarchyDto(noteHierarchyDto);
        return notePathAwareResponse;
    }
}
