package co.jinear.core.service.workspace;

import co.jinear.core.converter.workspace.WorkspaceDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.repository.WorkspaceRepository;
import co.jinear.core.service.workspace.member.WorkspaceMemberListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceRetrieveService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberListingService workspaceMemberListingService;
    private final WorkspaceDtoConverter workspaceDtoConverter;

    public WorkspaceDto retrieveWorkspaceWithId(String workspaceId) {
        log.info("Retrieving workspace with id: {}", workspaceId);
        return workspaceRepository.findByWorkspaceIdAndPassiveIdIsNull(workspaceId)
                .map(workspaceDtoConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public WorkspaceDto retrieveWorkspaceWithUsername(String workspaceUsername) {
        log.info("Retrieving workspace with username: {}", workspaceUsername);
        return workspaceRepository.findByUsername_UsernameAndUsername_PassiveIdIsNullAndPassiveIdIsNull(workspaceUsername)
                .map(workspaceDtoConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public List<DetailedWorkspaceMemberDto> retrieveAccountWorkspaces(String accountId) {
        return workspaceMemberListingService.retrieveAccountsWorkspaceMemberships(accountId);
    }
}
