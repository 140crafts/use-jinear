package co.jinear.core.manager.workspace;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.response.workspace.WorkspaceMemberListingBaseResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberListingService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceMemberRetrieveManager {
    private final int WORKSPACE_MEMBER_PAGE_SIZE = 50;

    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final WorkspaceMemberService workspaceMemberService;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceMemberListingService workspaceMemberListingService;
    private final WorkspaceValidator workspaceValidator;

    public WorkspaceMemberListingBaseResponse retrieveWorkspaceMembers(String workspaceId, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountIdInclAnonymous();
        log.info("Retrieve workspace members has started. workspaceId: {}, page: {}, currentAccountId: {}", workspaceId, page, currentAccountId);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(workspaceId);
        workspaceValidator.validateHasAccess(currentAccountId, workspaceDto);
        Page<WorkspaceMemberDto> workspaceMemberDtoPage = decideAndRetrievePage(page, currentAccountId, workspaceDto);
        return mapValues(workspaceMemberDtoPage);
    }

    private Page<WorkspaceMemberDto> decideAndRetrievePage(Integer page, String currentAccountId, WorkspaceDto workspaceDto) {
        boolean isMember = workspaceMemberService.isAccountWorkspaceMember(currentAccountId, workspaceDto.getWorkspaceId());
        return isMember ? workspaceMemberListingService.retrieveWorkspaceMembersDetailed(workspaceDto.getWorkspaceId(), PageRequest.of(page, WORKSPACE_MEMBER_PAGE_SIZE)) :
                workspaceMemberListingService.retrieveWorkspaceMembers(workspaceDto.getWorkspaceId(), PageRequest.of(page, WORKSPACE_MEMBER_PAGE_SIZE));
    }

    private WorkspaceMemberListingBaseResponse mapValues(Page<WorkspaceMemberDto> workspaceMemberDtoPage) {
        PageDto pageDto = new PageDto<>(workspaceMemberDtoPage);
        WorkspaceMemberListingBaseResponse workspaceMemberListingResponse = new WorkspaceMemberListingBaseResponse();
        workspaceMemberListingResponse.setWorkspaceMemberDtoPage(pageDto);
        return workspaceMemberListingResponse;
    }
}
