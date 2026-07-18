package co.jinear.core.manager.note;

import co.jinear.core.converter.note.NoteInitializeRequestToVoConverter;
import co.jinear.core.converter.note.NoteUpdateRequestToVoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
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
import co.jinear.core.service.note.IdempotentNoteCreateService;
import co.jinear.core.service.note.NoteOperationService;
import co.jinear.core.service.note.NoteRetrieveService;
import co.jinear.core.service.note.notebook.NotebookRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.validator.notebook.NotebookAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
    private final IdempotentNoteCreateService idempotentNoteCreateService;
    private final NoteInitializeRequestToVoConverter noteInitializeRequestToVoConverter;
    private final NoteUpdateRequestToVoConverter noteUpdateRequestToVoConverter;
    private final PassiveService passiveService;

    public NoteInitializeResponse initialize(String workspaceId, NoteInitializeRequest request) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        validateNotebookAccess(currentAccountId, request.getNotebookId());
        log.info("Initialize note has started. currentAccountId: {}", currentAccountId);
        NoteInitializeVo noteInitializeVo = noteInitializeRequestToVoConverter.convert(request, workspaceId, currentAccountId);
        NoteDto note = idempotentNoteCreateService.initialize(noteInitializeVo);
        return mapResponse(note);
    }

    public BaseResponse publish(String workspaceId, String noteId, String notebookId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NoteDto note = noteRetrieveService.retrieve(noteId);
        validateNotPublishedBefore(notebookId, note);
        String noteWorkspaceId = note.getWorkspaceId();
        validateWorkspaceAccessAndMatchPath(workspaceId, currentAccountId, noteWorkspaceId);
        validateNotebookAccess(currentAccountId, notebookId);
        NoteUpdateVo noteUpdateVo = NoteUpdateVo
                .builder()
                .noteId(noteId)
                .notebookId(notebookId)
                .build();
        noteOperationService.update(noteUpdateVo);
        return new BaseResponse();
    }

    public BaseResponse update(String workspaceId, NoteUpdateRequest request) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        validateNoteAccess(currentAccountId, request.getNoteId());
        log.info("Update note has started. currentAccountId: {}", currentAccountId);
        NoteUpdateVo noteUpdateVo = noteUpdateRequestToVoConverter.convert(request);
        noteOperationService.update(noteUpdateVo);
        return new BaseResponse();
    }

    public BaseResponse move(String workspaceId, NoteMoveRequest request) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        validateNoteAccess(currentAccountId, request.getNoteId());
        log.info("Move note has started. currentAccountId: {}", currentAccountId);
        noteOperationService.moveTo(request.getNoteId(), request.getParentNoteId());
        return new BaseResponse();
    }

    public BaseResponse delete(String workspaceId, String noteId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        validateNoteAccess(currentAccountId, noteId);
        log.info("Delete note has started. currentAccountId: {}", currentAccountId);
        String passiveId = passiveService.createUserActionPassive(currentAccountId);
        noteOperationService.delete(noteId, passiveId);
        return new BaseResponse();
    }

    private void validateNoteAccess(String currentAccountId, String noteId) {
        NoteDto noteDto = noteRetrieveService.retrieve(noteId);
        boolean isOwner = noteDto.getOwnerId().equalsIgnoreCase(currentAccountId);
        if (!isOwner) {
            validateNotebookAccess(currentAccountId, noteDto.getNotebookId());
        }
    }

    private void validateNotebookAccess(String currentAccountId, String notebookId) {
        if (Objects.nonNull(notebookId)) {
            NotebookDto notebookDto = notebookRetrieveService.retrieve(notebookId);
            notebookAccessValidator.validateHasAccess(currentAccountId, notebookDto);
        }
    }

    private void validateWorkspaceAccessAndMatchPath(String workspaceId, String currentAccountId, String noteWorkspaceId) {
        if (!workspaceId.equalsIgnoreCase(noteWorkspaceId)) {
            throw new NoAccessException();
        }
        workspaceValidator.validateHasAccess(currentAccountId, noteWorkspaceId);
    }

    private void validateNotPublishedBefore(String notebookId, NoteDto note) {
        String existingNotebookId = note.getNotebookId();
        if (Objects.nonNull(existingNotebookId) && !notebookId.equalsIgnoreCase(existingNotebookId)) {
            throw new BusinessException("note.published-before");
        }
    }

    private NoteInitializeResponse mapResponse(NoteDto noteDto) {
        NoteInitializeResponse response = new NoteInitializeResponse();
        response.setNoteDto(noteDto);
        return response;
    }
}
