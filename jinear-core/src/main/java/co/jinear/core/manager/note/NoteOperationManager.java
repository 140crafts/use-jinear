package co.jinear.core.manager.note;

import co.jinear.core.converter.note.NoteInitializeRequestToVoConverter;
import co.jinear.core.converter.note.NoteUpdateRequestToVoConverter;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.request.note.NoteInitializeRequest;
import co.jinear.core.model.request.note.NoteMoveRequest;
import co.jinear.core.model.request.note.NoteUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.note.NoteInitializeResponse;
import co.jinear.core.model.vo.note.NoteInitializeVo;
import co.jinear.core.model.vo.note.NoteUpdateVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.note.NoteOperationService;
import co.jinear.core.service.note.NoteRetrieveService;
import co.jinear.core.service.note.notebook.NotebookRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.validator.notebook.NotebookAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteOperationManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final NotebookAccessValidator notebookAccessValidator;
    private final NotebookRetrieveService notebookRetrieveService;
    private final NoteRetrieveService noteRetrieveService;
    private final NoteOperationService noteOperationService;
    private final NoteInitializeRequestToVoConverter noteInitializeRequestToVoConverter;
    private final NoteUpdateRequestToVoConverter noteUpdateRequestToVoConverter;
    private final PassiveService passiveService;

    public NoteInitializeResponse initialize(NoteInitializeRequest request) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NotebookDto notebookDto = notebookRetrieveService.retrieve(request.getNotebookId());
        validateNotebookAccess(currentAccountId, notebookDto);
        log.info("Initialize note has started. currentAccountId: {}", currentAccountId);
        NoteInitializeVo noteInitializeVo = noteInitializeRequestToVoConverter.convert(request, notebookDto.getWorkspaceId(), currentAccountId);
        String noteId = noteOperationService.initialize(noteInitializeVo);
        NoteInitializeResponse response = new NoteInitializeResponse();
        response.setNoteId(noteId);
        return response;
    }

    public BaseResponse update(NoteUpdateRequest request) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateNoteAccess(currentAccountId, request.getNoteId());
        log.info("Update note has started. currentAccountId: {}", currentAccountId);
        NoteUpdateVo noteUpdateVo = noteUpdateRequestToVoConverter.convert(request);
        noteOperationService.update(noteUpdateVo);
        return new BaseResponse();
    }

    public BaseResponse move(NoteMoveRequest request) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateNoteAccess(currentAccountId, request.getNoteId());
        log.info("Move note has started. currentAccountId: {}", currentAccountId);
        noteOperationService.moveTo(request.getNoteId(), request.getParentNoteId());
        return new BaseResponse();
    }

    public BaseResponse delete(String noteId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateNoteAccess(currentAccountId, noteId);
        log.info("Delete note has started. currentAccountId: {}", currentAccountId);
        String passiveId = passiveService.createUserActionPassive(currentAccountId);
        noteOperationService.delete(noteId, passiveId);
        return new BaseResponse();
    }

    private void validateNoteAccess(String currentAccountId, String noteId) {
        NoteDto noteDto = noteRetrieveService.retrieve(noteId);
        NotebookDto notebookDto = notebookRetrieveService.retrieve(noteDto.getNotebookId());
        validateNotebookAccess(currentAccountId, notebookDto);
    }

    private void validateNotebookAccess(String currentAccountId, NotebookDto notebookDto) {
        workspaceValidator.validateHasAccess(currentAccountId, notebookDto.getWorkspaceId());
        notebookAccessValidator.validateHasAccess(currentAccountId, notebookDto);
    }
}
