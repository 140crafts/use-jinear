package co.jinear.core.service.note.notebook;

import co.jinear.core.model.entity.note.NotebookMember;
import co.jinear.core.model.vo.notebook.AddNotebookMemberVo;
import co.jinear.core.repository.notebook.NotebookMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotebookMemberOperationService {

    private final NotebookMemberRepository notebookMemberRepository;
    private final NotebookMemberRetrieveService notebookMemberRetrieveService;

    public void addNotebookMember(AddNotebookMemberVo addNotebookMemberVo) {
        log.info("Add notebook member has started. addNotebookMemberVo: {}", addNotebookMemberVo);
        if (notebookMemberRetrieveService.isNotebookMember(addNotebookMemberVo.getAccountId(), addNotebookMemberVo.getNotebookId())) {
            log.info("Account is already a notebook member. Skipping. addNotebookMemberVo: {}", addNotebookMemberVo);
            return;
        }
        NotebookMember notebookMember = mapToEntity(addNotebookMemberVo);
        notebookMemberRepository.save(notebookMember);
        log.info("Add notebook member has completed.");
    }

    public void removeNotebookMember(String notebookId, String accountId, String passiveId) {
        log.info("Remove notebook member has started. notebookId: {}, accountId: {}, passiveId: {}", notebookId, accountId, passiveId);
        NotebookMember notebookMember = notebookMemberRetrieveService.retrieveEntity(accountId, notebookId);
        notebookMember.setPassiveId(passiveId);
        notebookMemberRepository.save(notebookMember);
        log.info("Notebook member removed with passiveId: {}", passiveId);
    }

    @Transactional
    public void removeAllMembers(String notebookId, String passiveId) {
        log.info("Remove all notebook members has started. notebookId: {}, passiveId: {}", notebookId, passiveId);
        notebookMemberRepository.removeAllMembers(notebookId, passiveId);
        log.info("All notebook members removed with passiveId: {}", passiveId);
    }

    private NotebookMember mapToEntity(AddNotebookMemberVo addNotebookMemberVo) {
        NotebookMember notebookMember = new NotebookMember();
        notebookMember.setAccountId(addNotebookMemberVo.getAccountId());
        notebookMember.setWorkspaceId(addNotebookMemberVo.getWorkspaceId());
        notebookMember.setNotebookId(addNotebookMemberVo.getNotebookId());
        return notebookMember;
    }
}
