package co.jinear.core.service.note.notetag;

import co.jinear.core.converter.notetag.NoteTagEntityConverter;
import co.jinear.core.model.entity.note.NoteTag;
import co.jinear.core.model.vo.notetag.NoteTagInitializeVo;
import co.jinear.core.model.vo.notetag.NoteTagUpdateVo;
import co.jinear.core.repository.notetag.NoteTagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteTagOperationService {

    private final NoteTagRepository noteTagRepository;
    private final NoteTagRetrieveService noteTagRetrieveService;
    private final NoteTagEntityConverter noteTagEntityConverter;
    private final NoteTagAssignmentOperationService noteTagAssignmentOperationService;

    public String initialize(NoteTagInitializeVo noteTagInitializeVo) {
        log.info("Initialize note tag has started. noteTagInitializeVo: {}", noteTagInitializeVo);
        NoteTag noteTag = noteTagEntityConverter.convert(noteTagInitializeVo);
        NoteTag saved = noteTagRepository.save(noteTag);
        return saved.getNoteTagId();
    }

    public void update(NoteTagUpdateVo noteTagUpdateVo) {
        log.info("Update note tag has started. noteTagUpdateVo: {}", noteTagUpdateVo);
        NoteTag noteTag = noteTagRetrieveService.retrieveEntity(noteTagUpdateVo.getNoteTagId());
        noteTag.setName(noteTagUpdateVo.getName());
        noteTag.setColor(noteTagUpdateVo.getColor());
        noteTagRepository.save(noteTag);
    }

    @Transactional
    public void delete(String noteTagId, String passiveId) {
        log.info("Delete note tag has started. noteTagId: {}, passiveId: {}", noteTagId, passiveId);
        noteTagAssignmentOperationService.removeAllByTag(noteTagId, passiveId);
        NoteTag noteTag = noteTagRetrieveService.retrieveEntity(noteTagId);
        noteTag.setPassiveId(passiveId);
        noteTagRepository.save(noteTag);
    }

    @Transactional
    public void deleteAllByNotebook(String notebookId, String passiveId) {
        log.info("Delete all note tags by notebook has started. notebookId: {}, passiveId: {}", notebookId, passiveId);
        noteTagRepository.passivizeAllByNotebookId(notebookId, passiveId);
    }
}
