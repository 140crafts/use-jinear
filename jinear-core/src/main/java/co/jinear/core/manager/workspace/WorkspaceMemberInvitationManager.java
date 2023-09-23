package co.jinear.core.manager.workspace;

import co.jinear.core.converter.workspace.WorkspaceInvitationInfoResponseConverter;
import co.jinear.core.converter.workspace.WorkspaceInvitationInitializeVoConverter;
import co.jinear.core.converter.workspace.WorkspaceInvitationRespondVoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.workspace.WorkspaceInvitationDto;
import co.jinear.core.model.dto.workspace.WorkspaceInvitationInfoDto;
import co.jinear.core.model.request.workspace.WorkspaceMemberInvitationRespondRequest;
import co.jinear.core.model.request.workspace.WorkspaceMemberInviteRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.workspace.WorkspaceInvitationInfoResponse;
import co.jinear.core.model.response.workspace.WorkspaceInvitationListingResponse;
import co.jinear.core.model.vo.workspace.WorkspaceInvitationInitializeVo;
import co.jinear.core.model.vo.workspace.WorkspaceInvitationRespondVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.workspace.member.WorkspaceInvitationListingService;
import co.jinear.core.service.workspace.member.WorkspaceInvitationOperationService;
import co.jinear.core.service.workspace.member.WorkspaceInvitationRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import co.jinear.core.validator.workspace.WorkspaceInvitationValidator;
import co.jinear.core.validator.workspace.WorkspaceTierValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.ADMIN;
import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.OWNER;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkspaceMemberInvitationManager {

    private final WorkspaceMemberService workspaceMemberService;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceInvitationOperationService workspaceInvitationOperationService;
    private final WorkspaceInvitationInitializeVoConverter workspaceInvitationInitializeVoConverter;
    private final WorkspaceValidator workspaceValidator;
    private final WorkspaceInvitationRespondVoConverter workspaceInvitationRespondVoConverter;
    private final AccountRetrieveService accountRetrieveService;
    private final WorkspaceInvitationInfoResponseConverter workspaceInvitationInfoResponseConverter;
    private final WorkspaceInvitationListingService workspaceInvitationListingService;
    private final WorkspaceInvitationRetrieveService workspaceInvitationRetrieveService;
    private final WorkspaceTierValidator workspaceTierValidator;
    private final WorkspaceInvitationValidator workspaceInvitationValidator;
    private final PassiveService passiveService;

    public WorkspaceInvitationListingResponse listInvitations(String workspaceId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceMemberService.validateAccountHasRoleInWorkspace(currentAccountId, workspaceId, List.of(OWNER, ADMIN));
        log.info("List workspace invitations has started. currentAccountId: {}", currentAccountId);
        Page<WorkspaceInvitationDto> workspaceInvitationDtos = workspaceInvitationListingService.retrieveActiveInvitations(workspaceId, page);
        return mapResponse(workspaceInvitationDtos);
    }

    public BaseResponse invite(WorkspaceMemberInviteRequest workspaceMemberInviteRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String workspaceId = workspaceMemberInviteRequest.getWorkspaceId();
        workspaceMemberService.validateAccountHasRoleInWorkspace(currentAccountId, workspaceId, List.of(OWNER, ADMIN));
        workspaceInvitationValidator.validateActiveInviteExists(workspaceId,workspaceMemberInviteRequest.getEmail());
        workspaceTierValidator.validateWorkspaceCanAddMember(workspaceId);
        validateInviteNotForOwnerRole(workspaceMemberInviteRequest);
        validateAccountIsExistingMemberIfAccountPresent(workspaceMemberInviteRequest, workspaceId);
        log.info("Invite workspace member has started. currentAccountId: {}", currentAccountId);
        WorkspaceInvitationInitializeVo vo = workspaceInvitationInitializeVoConverter.convert(workspaceMemberInviteRequest, currentAccountId);
        workspaceInvitationOperationService.initializeInvitation(vo);
        return new BaseResponse();
    }

    public BaseResponse delete(String invitationId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        WorkspaceInvitationDto workspaceInvitationDto = workspaceInvitationRetrieveService.retrieve(invitationId);
        String workspaceId = workspaceInvitationDto.getWorkspaceId();
        workspaceMemberService.validateAccountHasRoleInWorkspace(currentAccountId, workspaceId, List.of(OWNER, ADMIN));
        log.info("Delete workspace member invitation has started. currentAccountId: {}", currentAccountId);
        String passiveId = workspaceInvitationOperationService.deleteInvitation(invitationId);
        passiveService.assignOwnership(passiveId,currentAccountId);
        return null;
    }

    public WorkspaceInvitationInfoResponse retrieveInvitationInfo(String token) {
        String currentAccountId = sessionInfoService.currentAccountIdInclAnonymous();
        log.info("Retrieve invitation info has started. currentAccountId: {}", currentAccountId);
        WorkspaceInvitationInfoDto workspaceInvitationInfoDto = workspaceInvitationOperationService.retrieveInvitationInfo(token);
        return workspaceInvitationInfoResponseConverter.convert(workspaceInvitationInfoDto);
    }

    public BaseResponse respondInvitation(WorkspaceMemberInvitationRespondRequest workspaceMemberInvitationRespondRequest) {
        log.info("Respond workspace invitation has started");
        Optional<String> optionalCurrentAccountId = sessionInfoService.currentAccountIdOptional();
        WorkspaceInvitationRespondVo workspaceInvitationRespondVo = workspaceInvitationRespondVoConverter.convert(workspaceMemberInvitationRespondRequest, optionalCurrentAccountId, accountRetrieveService);
        workspaceInvitationOperationService.decideAndFinalizeInvitation(workspaceInvitationRespondVo);
        return new BaseResponse();
    }


    private void validateInviteNotForOwnerRole(WorkspaceMemberInviteRequest workspaceMemberInviteRequest) {
        if (OWNER.equals(workspaceMemberInviteRequest.getForRole())) {
            throw new BusinessException();
        }
    }

    private void validateAccountIsExistingMemberIfAccountPresent(WorkspaceMemberInviteRequest workspaceMemberInviteRequest, String workspaceId) {
        String accountId = accountRetrieveService.retrieveByEmail(workspaceMemberInviteRequest.getEmail()).map(AccountDto::getAccountId).orElse(null);
        if (Objects.nonNull(accountId) && workspaceMemberService.isAccountWorkspaceMember(accountId, workspaceId)) {
            throw new BusinessException("workspace.invitation.account-already-member");
        }
    }

    private WorkspaceInvitationListingResponse mapResponse(Page<WorkspaceInvitationDto> workspaceInvitationDtos) {
        WorkspaceInvitationListingResponse workspaceInvitationListingResponse = new WorkspaceInvitationListingResponse();
        workspaceInvitationListingResponse.setWorkspaceInvitationDtoList(new PageDto<>(workspaceInvitationDtos));
        return workspaceInvitationListingResponse;
    }
}
