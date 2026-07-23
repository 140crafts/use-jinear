package co.jinear.core.manager.notetag;

import co.jinear.core.converter.notetag.NoteTagInitializeRequestToVoConverter;
import co.jinear.core.converter.notetag.NoteTagUpdateRequestToVoConverter;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.dto.notetag.NoteTagDto;
import co.jinear.core.model.request.notetag.AssignNoteTagRequest;
import co.jinear.core.model.request.notetag.NoteTagInitializeRequest;
import co.jinear.core.model.request.notetag.NoteTagUpdateRequest;
import co.jinear.core.model.request.notetag.UpdateNoteTagAssignmentsRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.notetag.NoteTagInitializeResponse;
import co.jinear.core.model.response.notetag.NoteTagListingResponse;
import co.jinear.core.model.vo.notetag.NoteTagInitializeVo;
import co.jinear.core.model.vo.notetag.NoteTagUpdateVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.note.NoteRetrieveService;
import co.jinear.core.service.note.notebook.NotebookRetrieveService;
import co.jinear.core.service.note.notetag.NoteTagAssignmentOperationService;
import co.jinear.core.service.note.notetag.NoteTagOperationService;
import co.jinear.core.service.note.notetag.NoteTagRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.validator.notebook.NotebookAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteTagManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final NotebookAccessValidator notebookAccessValidator;
    private final NotebookRetrieveService notebookRetrieveService;
    private final NoteRetrieveService noteRetrieveService;
    private final NoteTagRetrieveService noteTagRetrieveService;
    private final NoteTagOperationService noteTagOperationService;
    private final NoteTagAssignmentOperationService noteTagAssignmentOperationService;
    private final NoteTagInitializeRequestToVoConverter noteTagInitializeRequestToVoConverter;
    private final NoteTagUpdateRequestToVoConverter noteTagUpdateRequestToVoConverter;
    private final PassiveService passiveService;

    public NoteTagInitializeResponse create(NoteTagInitializeRequest request) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NotebookDto notebookDto = validateNotebookAccessById(currentAccountId, request.getNotebookId());
        log.info("Create note tag has started. currentAccountId: {}", currentAccountId);
        NoteTagInitializeVo noteTagInitializeVo = noteTagInitializeRequestToVoConverter.convert(request, notebookDto.getWorkspaceId());
        String noteTagId = noteTagOperationService.initialize(noteTagInitializeVo);
        NoteTagInitializeResponse response = new NoteTagInitializeResponse();
        response.setNoteTagId(noteTagId);
        return response;
    }

    public BaseResponse update(NoteTagUpdateRequest request) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NoteTagDto noteTagDto = noteTagRetrieveService.retrieve(request.getNoteTagId());
        validateNotebookAccessById(currentAccountId, noteTagDto.getNotebookId());
        log.info("Update note tag has started. currentAccountId: {}", currentAccountId);
        NoteTagUpdateVo noteTagUpdateVo = noteTagUpdateRequestToVoConverter.convert(request);
        noteTagOperationService.update(noteTagUpdateVo);
        return new BaseResponse();
    }

    public BaseResponse delete(String noteTagId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NoteTagDto noteTagDto = noteTagRetrieveService.retrieve(noteTagId);
        validateNotebookAccessById(currentAccountId, noteTagDto.getNotebookId());
        log.info("Delete note tag has started. currentAccountId: {}", currentAccountId);
        String passiveId = passiveService.createUserActionPassive(currentAccountId);
        noteTagOperationService.delete(noteTagId, passiveId);
        return new BaseResponse();
    }

    public NoteTagListingResponse list(String notebookId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateNotebookAccessById(currentAccountId, notebookId);
        List<NoteTagDto> tags = noteTagRetrieveService.retrieveNotebookTags(notebookId);
        NoteTagListingResponse response = new NoteTagListingResponse();
        response.setNoteTagDtoList(tags);
        return response;
    }

    public BaseResponse assign(AssignNoteTagRequest request) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NoteDto noteDto = noteRetrieveService.retrieve(request.getNoteId());
        validateNotebookAccessById(currentAccountId, noteDto.getNotebookId());
        validateTagIsWithinNotebook(request.getNoteTagId(), noteDto.getNotebookId());
        log.info("Assign note tag has started. currentAccountId: {}", currentAccountId);
        noteTagAssignmentOperationService.assign(request.getNoteId(), request.getNoteTagId());
        return new BaseResponse();
    }

    public BaseResponse updateAssignments(UpdateNoteTagAssignmentsRequest request) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NoteDto noteDto = noteRetrieveService.retrieve(request.getNoteId());
        validateNotebookAccessById(currentAccountId, noteDto.getNotebookId());
        request.getNoteTagIds().forEach(noteTagId -> validateTagIsWithinNotebook(noteTagId, noteDto.getNotebookId()));
        log.info("Update note tag assignments has started. currentAccountId: {}", currentAccountId);
        String passiveId = noteTagAssignmentOperationService.updateAs(request.getNoteId(), request.getNoteTagIds());
        assignPassiveIdOwnershipIfExists(currentAccountId, passiveId);
        return new BaseResponse();
    }

    public BaseResponse unassign(AssignNoteTagRequest request) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NoteDto noteDto = noteRetrieveService.retrieve(request.getNoteId());
        validateNotebookAccessById(currentAccountId, noteDto.getNotebookId());
        log.info("Unassign note tag has started. currentAccountId: {}", currentAccountId);
        String passiveId = passiveService.createUserActionPassive(currentAccountId);
        noteTagAssignmentOperationService.unassign(request.getNoteId(), request.getNoteTagId(), passiveId);
        return new BaseResponse();
    }

    private void assignPassiveIdOwnershipIfExists(String currentAccountId, String passiveId) {
        if (Objects.nonNull(passiveId)) {
            passiveService.assignOwnership(passiveId, currentAccountId);
        }
    }

    private NotebookDto validateNotebookAccessById(String currentAccountId, String notebookId) {
        NotebookDto notebookDto = notebookRetrieveService.retrieve(notebookId);
        workspaceValidator.validateHasAccess(currentAccountId, notebookDto.getWorkspaceId());
        notebookAccessValidator.validateHasAccess(currentAccountId, notebookDto);
        return notebookDto;
    }

    private void validateTagIsWithinNotebook(String noteTagId, String notebookId) {
        if (!noteTagRetrieveService.existsByNoteTagIdAndNotebookId(noteTagId, notebookId)) {
            log.info("Note tag is not within the note's notebook. noteTagId: {}, notebookId: {}", noteTagId, notebookId);
            throw new NotValidException();
        }
    }
}
