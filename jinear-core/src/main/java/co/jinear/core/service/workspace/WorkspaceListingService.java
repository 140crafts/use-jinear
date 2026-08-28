package co.jinear.core.service.workspace;

import co.jinear.core.converter.workspace.WorkspaceDtoConverter;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceListingService {

    private static final int PAGE_SIZE = 50;

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceDtoConverter workspaceDtoConverter;

    public Page<WorkspaceDto> retrieveAllWorkspaces(int page) {
        log.info("Retrieve all workspaces has started. page: {}", page);
        return workspaceRepository.findAllByPassiveIdIsNullOrderByCreatedDateDesc(PageRequest.of(page, PAGE_SIZE))
                .map(workspaceDtoConverter::map);
    }
}
