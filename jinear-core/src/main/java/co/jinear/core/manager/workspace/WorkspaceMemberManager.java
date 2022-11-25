package co.jinear.core.manager.workspace;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.workspace.WorkspaceMemberListingBaseResponse;
import co.jinear.core.model.vo.workspace.DeleteWorkspaceMemberVo;
import co.jinear.core.model.vo.workspace.InitializeWorkspaceMemberVo;
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
public class WorkspaceMemberManager {

    private final int WORKSPACE_MEMBER_PAGE_SIZE = 50;

    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final WorkspaceMemberService workspaceMemberService;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceMemberListingService workspaceMemberListingService;
    private final WorkspaceValidator workspaceValidator;

    public WorkspaceMemberListingBaseResponse retrieveWorkspaceMembers(String workspaceUsername, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountIdInclAnonymous();
        log.info("Retrieve workspace members has started. workspaceUsername: {}, page: {}, currentAccountId: {}", workspaceUsername, page, currentAccountId);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithUsername(workspaceUsername);
        workspaceValidator.validateHasAccess(currentAccountId, workspaceDto);
        Page<WorkspaceMemberDto> workspaceMemberDtoPage = decideAndRetrievePage(page, currentAccountId, workspaceDto);
        return mapValues(workspaceMemberDtoPage);
    }

    public BaseResponse joinWorkspace(String workspaceUsername) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Join workspace has started. workspaceUsername: {}, currentAccountId: {}", workspaceUsername, currentAccountId);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithUsername(workspaceUsername);
        InitializeWorkspaceMemberVo initializeWorkspaceMemberVo = mapValues(currentAccountId, workspaceDto);
        workspaceMemberService.initializeWorkspaceMember(initializeWorkspaceMemberVo);
        return new BaseResponse();
    }

    public BaseResponse leaveWorkspace(String workspaceUsername) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Leave workspace has started. workspaceUsername: {}, currentAccountId: {}", workspaceUsername, currentAccountId);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithUsername(workspaceUsername);
        workspaceMemberService.validateAccountIsNotWorkspaceOwner(currentAccountId, workspaceDto.getWorkspaceId());
        DeleteWorkspaceMemberVo deleteWorkspaceMemberVo = mapToDeleteWorkspaceMember(currentAccountId, workspaceDto);
        workspaceMemberService.deleteWorkspaceMember(deleteWorkspaceMemberVo);
        return new BaseResponse();
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

    private InitializeWorkspaceMemberVo mapValues(String currentAccountId, WorkspaceDto workspaceDto) {
        InitializeWorkspaceMemberVo initializeWorkspaceMemberVo = new InitializeWorkspaceMemberVo();
        initializeWorkspaceMemberVo.setWorkspaceId(workspaceDto.getWorkspaceId());
        initializeWorkspaceMemberVo.setAccountId(currentAccountId);
        initializeWorkspaceMemberVo.setRole(WorkspaceAccountRoleType.MEMBER);
        return initializeWorkspaceMemberVo;
    }

    private DeleteWorkspaceMemberVo mapToDeleteWorkspaceMember(String currentAccountId, WorkspaceDto workspaceDto) {
        DeleteWorkspaceMemberVo deleteWorkspaceMemberVo = new DeleteWorkspaceMemberVo();
        deleteWorkspaceMemberVo.setWorkspaceId(workspaceDto.getWorkspaceId());
        deleteWorkspaceMemberVo.setAccountId(currentAccountId);
        return deleteWorkspaceMemberVo;
    }
}