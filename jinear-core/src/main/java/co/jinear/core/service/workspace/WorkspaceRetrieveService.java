package co.jinear.core.service.workspace;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.repository.WorkspaceRepository;
import co.jinear.core.service.workspace.member.WorkspaceMemberListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceRetrieveService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberListingService workspaceMemberListingService;
    private final ModelMapper modelMapper;

    public WorkspaceDto retrieveWorkspaceWithId(String workspaceId) {
        log.info("Retrieving workspace with id: {}", workspaceId);
        return workspaceRepository.findByWorkspaceIdAndPassiveIdIsNull(workspaceId)
                .map(workspace -> modelMapper.map(workspace, WorkspaceDto.class))
                .orElseThrow(NotFoundException::new);
    }

    public WorkspaceDto retrieveWorkspaceWithUsername(String workspaceUsername) {
        log.info("Retrieving workspace with username: {}", workspaceUsername);
        return workspaceRepository.findByUsername_UsernameAndUsername_PassiveIdIsNullAndPassiveIdIsNull(workspaceUsername)
                .map(workspace -> modelMapper.map(workspace, WorkspaceDto.class))
                .orElseThrow(NotFoundException::new);
    }

    public List<WorkspaceDto> retrieveAllById(List<String> workspaceIds) {
        log.info("Retrieve all by workspace id has started. workspaceIds", StringUtils.join(workspaceIds, ", "));
        return workspaceRepository.findAllByWorkspaceIdIsInAndPassiveIdIsNull(workspaceIds)
                .stream()
                .map(workspace -> modelMapper.map(workspace, WorkspaceDto.class))
                .toList();
    }

    public List<WorkspaceDto> retrieveAccountWorkspace(String accountId) {
        List<String> accountWorkspaceIds = workspaceMemberListingService.retrieveWorkspaceMembersByAccountId(accountId)
                .stream()
                .map(WorkspaceMemberDto::getWorkspaceId)
                .toList();
        return retrieveAllById(accountWorkspaceIds);
    }
}
