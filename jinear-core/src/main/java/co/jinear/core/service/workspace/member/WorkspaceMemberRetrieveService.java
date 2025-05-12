package co.jinear.core.service.workspace.member;

import co.jinear.core.converter.workspace.WorkspaceMemberDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.entity.workspace.WorkspaceMember;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.repository.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceMemberRetrieveService {

    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final WorkspaceMemberDtoConverter workspaceMemberDtoConverter;

    public WorkspaceAccountRoleType retrieveAccountWorkspaceRole(String accountId, String workspaceId) {
        log.info("Retrieve account workspace role has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        return workspaceMemberRepository.findByAccountIdAndWorkspaceIdAndPassiveIdIsNull(accountId, workspaceId)
                .map(WorkspaceMember::getRole)
                .orElseThrow(NotFoundException::new);
    }

    public Optional<WorkspaceAccountRoleType> retrieveAccountWorkspaceRoleOptional(String accountId, String workspaceId) {
        log.info("Retrieve account workspace role has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        return workspaceMemberRepository.findByAccountIdAndWorkspaceIdAndPassiveIdIsNull(accountId, workspaceId)
                .map(WorkspaceMember::getRole);
    }

    public WorkspaceMemberDto retrieve(String workspaceMemberId, String workspaceId) {
        log.info("Retrieve workspace member has started. workspaceMemberId: {}", workspaceMemberId);
        return workspaceMemberRepository.findByWorkspaceMemberIdAndWorkspaceIdAndPassiveIdIsNull(workspaceMemberId, workspaceId)
                .map(workspaceMemberDtoConverter::map)
                .orElseThrow(NotFoundException::new);
    }
}
