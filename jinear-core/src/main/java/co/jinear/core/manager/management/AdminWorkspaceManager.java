package co.jinear.core.manager.management;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import co.jinear.core.model.request.management.AdminWorkspaceInitializeRequest;
import co.jinear.core.model.request.management.AdminWorkspaceMemberAddRequest;
import co.jinear.core.model.request.workspace.WorkspaceTitleUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.management.AdminWorkspaceListingResponse;
import co.jinear.core.model.response.workspace.WorkspaceBaseResponse;
import co.jinear.core.model.response.workspace.WorkspaceMemberListingBaseResponse;
import co.jinear.core.model.vo.workspace.InitializeWorkspaceMemberVo;
import co.jinear.core.model.vo.workspace.WorkspaceInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.workspace.WorkspaceDeleteService;
import co.jinear.core.service.workspace.WorkspaceInitializeService;
import co.jinear.core.service.workspace.WorkspaceListingService;
import co.jinear.core.service.workspace.WorkspaceTierService;
import co.jinear.core.service.workspace.WorkspaceUpdateService;
import co.jinear.core.service.workspace.member.WorkspaceMemberListingService;
import co.jinear.core.service.workspace.member.WorkspaceMemberRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminWorkspaceManager {

    private static final int WORKSPACE_MEMBER_PAGE_SIZE = 250;

    private final SessionInfoService sessionInfoService;
    private final WorkspaceListingService workspaceListingService;
    private final WorkspaceInitializeService workspaceInitializeService;
    private final WorkspaceUpdateService workspaceUpdateService;
    private final WorkspaceTierService workspaceTierService;
    private final WorkspaceDeleteService workspaceDeleteService;
    private final WorkspaceMemberListingService workspaceMemberListingService;
    private final WorkspaceMemberRetrieveService workspaceMemberRetrieveService;
    private final WorkspaceMemberService workspaceMemberService;
    private final AccountRetrieveService accountRetrieveService;

    public AdminWorkspaceListingResponse retrieveAllWorkspaces(Integer page) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Admin retrieve all workspaces has started. accountId: {}, page: {}", accountId, page);
        Page<WorkspaceDto> workspaceDtoPage = workspaceListingService.retrieveAllWorkspaces(page);
        return mapResponse(workspaceDtoPage);
    }

    public WorkspaceBaseResponse initializeWorkspace(AdminWorkspaceInitializeRequest request) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Admin initialize workspace has started. accountId: {}, request: {}", accountId, request);
        accountRetrieveService.retrieve(request.getOwnerAccountId());
        WorkspaceInitializeVo workspaceInitializeVo = mapInitializeVo(request);
        WorkspaceDto workspaceDto = workspaceInitializeService.initializeWorkspace(workspaceInitializeVo);
        WorkspaceBaseResponse workspaceBaseResponse = new WorkspaceBaseResponse();
        workspaceBaseResponse.setWorkspace(workspaceDto);
        return workspaceBaseResponse;
    }

    public BaseResponse updateTitle(String workspaceId, WorkspaceTitleUpdateRequest request) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Admin update workspace title has started. accountId: {}, workspaceId: {}, request: {}", accountId, workspaceId, request);
        workspaceUpdateService.updateWorkspaceTitle(workspaceId, request.getTitle());
        return new BaseResponse();
    }

    public BaseResponse updateTier(String workspaceId, WorkspaceTier workspaceTier) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Admin update workspace tier has started. accountId: {}, workspaceId: {}, workspaceTier: {}", accountId, workspaceId, workspaceTier);
        workspaceTierService.updateWorkspaceTier(workspaceId, workspaceTier);
        return new BaseResponse();
    }

    public BaseResponse deleteWorkspace(String workspaceId) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Admin delete workspace has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        workspaceDeleteService.deleteWorkspace(workspaceId, accountId);
        return new BaseResponse();
    }

    public WorkspaceMemberListingBaseResponse retrieveWorkspaceMembers(String workspaceId, Integer page) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Admin retrieve workspace members has started. accountId: {}, workspaceId: {}, page: {}", accountId, workspaceId, page);
        Page<WorkspaceMemberDto> workspaceMemberDtoPage = workspaceMemberListingService.retrieveWorkspaceMembersDetailed(workspaceId, PageRequest.of(page, WORKSPACE_MEMBER_PAGE_SIZE));
        WorkspaceMemberListingBaseResponse response = new WorkspaceMemberListingBaseResponse();
        response.setWorkspaceMemberDtoPage(new PageDto<>(workspaceMemberDtoPage));
        return response;
    }

    public BaseResponse addWorkspaceMember(String workspaceId, AdminWorkspaceMemberAddRequest request) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Admin add workspace member has started. accountId: {}, workspaceId: {}, request: {}", accountId, workspaceId, request);
        accountRetrieveService.retrieve(request.getAccountId());
        InitializeWorkspaceMemberVo initializeWorkspaceMemberVo = new InitializeWorkspaceMemberVo();
        initializeWorkspaceMemberVo.setAccountId(request.getAccountId());
        initializeWorkspaceMemberVo.setWorkspaceId(workspaceId);
        initializeWorkspaceMemberVo.setRole(request.getRole());
        workspaceMemberService.initializeWorkspaceMember(initializeWorkspaceMemberVo);
        return new BaseResponse();
    }

    public BaseResponse removeWorkspaceMember(String workspaceId, String workspaceMemberId) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Admin remove workspace member has started. accountId: {}, workspaceId: {}, workspaceMemberId: {}", accountId, workspaceId, workspaceMemberId);
        WorkspaceMemberDto workspaceMemberDto = workspaceMemberRetrieveService.retrieve(workspaceMemberId, workspaceId);
        validateNotLastOwner(workspaceId, workspaceMemberDto);
        workspaceMemberService.deleteWorkspaceMember(workspaceMemberId);
        return new BaseResponse();
    }

    private void validateNotLastOwner(String workspaceId, WorkspaceMemberDto workspaceMemberDto) {
        if (WorkspaceAccountRoleType.OWNER.equals(workspaceMemberDto.getRole())
                && workspaceMemberService.countWorkspaceMembersWithRole(workspaceId, WorkspaceAccountRoleType.OWNER) <= 1L) {
            throw new BusinessException("workspace.member.last-owner");
        }
    }

    private WorkspaceInitializeVo mapInitializeVo(AdminWorkspaceInitializeRequest request) {
        WorkspaceInitializeVo workspaceInitializeVo = new WorkspaceInitializeVo();
        workspaceInitializeVo.setOwnerId(request.getOwnerAccountId());
        workspaceInitializeVo.setTitle(request.getTitle());
        workspaceInitializeVo.setDescription(request.getDescription());
        workspaceInitializeVo.setHandle(request.getHandle());
        workspaceInitializeVo.setVisibility(request.getVisibility());
        workspaceInitializeVo.setJoinType(request.getJoinType());
        workspaceInitializeVo.setAppendRandomStrOnCollision(Boolean.TRUE);
        workspaceInitializeVo.setLocale(request.getLocale());
        return workspaceInitializeVo;
    }

    private AdminWorkspaceListingResponse mapResponse(Page<WorkspaceDto> workspaceDtoPage) {
        AdminWorkspaceListingResponse adminWorkspaceListingResponse = new AdminWorkspaceListingResponse();
        adminWorkspaceListingResponse.setWorkspaceDtoPage(new PageDto<>(workspaceDtoPage));
        return adminWorkspaceListingResponse;
    }
}
