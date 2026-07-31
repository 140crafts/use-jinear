package co.jinear.core.service.note.notebook;

import co.jinear.core.converter.notebook.NotebookMemberDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.notebook.NotebookMemberDto;
import co.jinear.core.model.entity.note.NotebookMember;
import co.jinear.core.repository.notebook.NotebookMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotebookMemberRetrieveService {

    private static final int PAGE_SIZE = 50;

    private final NotebookMemberRepository notebookMemberRepository;
    private final NotebookMemberDtoConverter notebookMemberDtoConverter;

    public NotebookMember retrieveEntity(String accountId, String notebookId) {
        return notebookMemberRepository.findFirstByAccountIdAndNotebookIdAndPassiveIdIsNull(accountId, notebookId)
                .orElseThrow(NotFoundException::new);
    }

    public boolean isNotebookMember(String accountId, String notebookId) {
        log.info("Is notebook member has started. accountId: {}, notebookId: {}", accountId, notebookId);
        return notebookMemberRepository.existsByAccountIdAndNotebookIdAndPassiveIdIsNull(accountId, notebookId);
    }

    public Page<NotebookMemberDto> retrieveNotebookMembers(String notebookId, int page) {
        log.info("Retrieve notebook members has started. notebookId: {}, page: {}", notebookId, page);
        return notebookMemberRepository.findAllByNotebookIdAndPassiveIdIsNull(notebookId, PageRequest.of(page, PAGE_SIZE))
                .map(notebookMemberDtoConverter::convert);
    }
}
