package co.jinear.core.manager.note;

import co.jinear.core.converter.note.NoteCreateVoConverter;
import co.jinear.core.model.dto.note.NoteContentDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.note.NoteEditResultDto;
import co.jinear.core.model.vo.note.NoteCreateVo;
import co.jinear.core.model.vo.note.NoteEditVo;
import co.jinear.core.model.vo.note.NoteInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.note.NoteOperationService;
import co.jinear.core.service.note.NoteRetrieveService;
import co.jinear.core.service.note.content.NoteContentEditService;
import co.jinear.core.service.note.content.NoteContentRetrieveService;
import co.jinear.core.model.vo.note.change.WholeBodyChange;
import co.jinear.core.validator.note.NoteAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteContentManager {

    private static final String EMPTY_DOC_STATE = "AAA=";

    private final SessionInfoService sessionInfoService;
    private final NoteRetrieveService noteRetrieveService;
    private final NoteOperationService noteOperationService;
    private final NoteContentRetrieveService noteContentRetrieveService;
    private final NoteContentEditService noteContentEditService;
    private final NoteAccessValidator noteAccessValidator;
    private final NoteCreateVoConverter noteCreateVoConverter;

    public NoteContentDto retrieve(NoteDto note) {
        return noteContentRetrieveService.retrieve(note);
    }

    public NoteEditResultDto create(NoteCreateVo vo) {
        String currentAccountId = sessionInfoService.currentAccountId();
        noteAccessValidator.validateCanCreate(currentAccountId, vo.getWorkspaceId(), vo.getNotebookId());
        log.info("Create note content has started. currentAccountId: {}", currentAccountId);
        NoteInitializeVo initializeVo = noteCreateVoConverter.convert(vo, currentAccountId, EMPTY_DOC_STATE);
        NoteDto note = noteOperationService.initialize(initializeVo);
        NoteEditVo editVo = mapInitialContent(vo, note);
        return noteContentEditService.edit(note, editVo);
    }

    public NoteEditResultDto edit(NoteEditVo vo) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NoteDto note = noteRetrieveService.retrieve(vo.getNoteId());
        noteAccessValidator.validateCanEdit(currentAccountId, vo.getWorkspaceId(), note);
        return noteContentEditService.edit(note, vo);
    }

    private NoteEditVo mapInitialContent(NoteCreateVo vo, NoteDto note) {
        NoteEditVo editVo = new NoteEditVo();
        editVo.setWorkspaceId(vo.getWorkspaceId());
        editVo.setNoteId(note.getNoteId());
        editVo.setTitle(vo.getTitle());
        Optional.ofNullable(vo.getBody()).map(WholeBodyChange::new).ifPresent(editVo::setBodyChange);
        return editVo;
    }
}
