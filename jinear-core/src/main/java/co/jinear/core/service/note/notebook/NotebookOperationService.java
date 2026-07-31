package co.jinear.core.service.note.notebook;

import co.jinear.core.converter.notebook.NotebookEntityConverter;
import co.jinear.core.model.entity.note.Notebook;
import co.jinear.core.model.enumtype.notebook.NotebookVisibilityType;
import co.jinear.core.model.vo.notebook.NotebookInitializeVo;
import co.jinear.core.model.vo.notebook.NotebookUpdateVo;
import co.jinear.core.repository.notebook.NotebookRepository;
import co.jinear.core.service.note.NoteOperationService;
import co.jinear.core.service.note.notetag.NoteTagOperationService;
import co.jinear.core.service.passive.PassiveService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotebookOperationService {

    private final NotebookRepository notebookRepository;
    private final NotebookRetrieveService notebookRetrieveService;
    private final NotebookEntityConverter notebookEntityConverter;
    private final NotebookMemberOperationService notebookMemberOperationService;
    private final NoteOperationService noteOperationService;
    private final NoteTagOperationService noteTagOperationService;
    private final PassiveService passiveService;

    public String initialize(NotebookInitializeVo notebookInitializeVo) {
        log.info("Initialize notebook has started. notebookInitializeVo: {}", notebookInitializeVo);
        Notebook notebook = notebookEntityConverter.convert(notebookInitializeVo);
        Notebook saved = notebookRepository.save(notebook);
        return saved.getNotebookId();
    }

    public void update(NotebookUpdateVo notebookUpdateVo) {
        log.info("Update notebook has started. notebookUpdateVo: {}", notebookUpdateVo);
        Notebook notebook = notebookRetrieveService.retrieveEntity(notebookUpdateVo.getNotebookId());
        notebook.setTitle(notebookUpdateVo.getTitle());
        notebook.setDescription(notebookUpdateVo.getDescription());
        if (Objects.nonNull(notebookUpdateVo.getVisibility())) {
            updateVisibility(notebook, notebookUpdateVo.getVisibility());
        }
        notebookRepository.save(notebook);
    }

    @Transactional
    public String delete(String notebookId) {
        String passiveId = passiveService.createUserActionPassive();
        log.info("Delete notebook has started. notebookId: {}, passiveId: {}", notebookId, passiveId);
        notebookMemberOperationService.removeAllMembers(notebookId, passiveId);
        noteOperationService.deleteAllByNotebook(notebookId, passiveId);
        noteTagOperationService.deleteAllByNotebook(notebookId, passiveId);
        Notebook notebook = notebookRetrieveService.retrieveEntity(notebookId);
        notebook.setPassiveId(passiveId);
        notebookRepository.save(notebook);
        return passiveId;
    }

    private void updateVisibility(Notebook notebook, NotebookVisibilityType nextVisibility) {
        notebook.setVisibility(nextVisibility);
    }
}
