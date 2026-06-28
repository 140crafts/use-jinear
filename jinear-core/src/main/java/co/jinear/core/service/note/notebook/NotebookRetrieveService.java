package co.jinear.core.service.note.notebook;

import co.jinear.core.converter.notebook.NotebookDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.entity.note.Notebook;
import co.jinear.core.repository.notebook.NotebookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotebookRetrieveService {

    private static final int PAGE_SIZE = 50;

    private final NotebookRepository notebookRepository;
    private final NotebookDtoConverter notebookDtoConverter;

    public NotebookDto retrieve(String notebookId) {
        log.info("Retrieve notebook has started. notebookId: {}", notebookId);
        return notebookRepository.findByNotebookIdAndPassiveIdIsNull(notebookId)
                .map(notebookDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public Notebook retrieveEntity(String notebookId) {
        return notebookRepository.findByNotebookIdAndPassiveIdIsNull(notebookId)
                .orElseThrow(NotFoundException::new);
    }

    public boolean existsByNotebookIdAndWorkspaceId(String notebookId, String workspaceId) {
        return notebookRepository.existsByNotebookIdAndWorkspaceIdAndPassiveIdIsNull(notebookId, workspaceId);
    }

    public Page<NotebookDto> retrieveWorkspaceNotebooks(String workspaceId, int page) {
        log.info("Retrieve workspace notebooks has started. workspaceId: {}, page: {}", workspaceId, page);
        return notebookRepository.findAllByWorkspaceIdAndPassiveIdIsNullOrderByCreatedDateDesc(workspaceId, PageRequest.of(page, PAGE_SIZE))
                .map(notebookDtoConverter::convert);
    }

    public Page<NotebookDto> retrieveAccessibleNotebooks(String workspaceId, String accountId, int page) {
        log.info("Retrieve accessible notebooks has started. workspaceId: {}, accountId: {}, page: {}", workspaceId, accountId, page);
        return notebookRepository.findAccessibleNotebooks(workspaceId, accountId, PageRequest.of(page, PAGE_SIZE))
                .map(notebookDtoConverter::convert);
    }
}
