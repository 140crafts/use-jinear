package co.jinear.core.manager.workspace;

import co.jinear.core.converter.workspace.WorkspaceInvitationInfoResponseConverter;
import co.jinear.core.converter.workspace.WorkspaceInvitationInitializeVoConverter;
import co.jinear.core.converter.workspace.WorkspaceInvitationRespondVoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceInvitationInfoDto;
import co.jinear.core.model.dto.workspace.WorkspaceSettingDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.model.enumtype.workspace.WorkspaceJoinType;
import co.jinear.core.model.request.workspace.WorkspaceMemberInvitationRespondRequest;
import co.jinear.core.model.request.workspace.WorkspaceMemberInviteRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.workspace.WorkspaceInvitationInfoResponse;
import co.jinear.core.model.vo.workspace.DeleteWorkspaceMemberVo;
import co.jinear.core.model.vo.workspace.InitializeWorkspaceMemberVo;
import co.jinear.core.model.vo.workspace.WorkspaceInvitationInitializeVo;
import co.jinear.core.model.vo.workspace.WorkspaceInvitationRespondVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceInvitationOperationService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.ADMIN;
import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.OWNER;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceMemberManager {

    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final WorkspaceMemberService workspaceMemberService;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceInvitationOperationService workspaceInvitationOperationService;
    private final WorkspaceInvitationInitializeVoConverter workspaceInvitationInitializeVoConverter;
    private final WorkspaceValidator workspaceValidator;
    private final WorkspaceInvitationRespondVoConverter workspaceInvitationRespondVoConverter;
    private final AccountRetrieveService accountRetrieveService;
    private final WorkspaceInvitationInfoResponseConverter workspaceInvitationInfoResponseConverter;

    @Transactional
    public BaseResponse invite(WorkspaceMemberInviteRequest workspaceMemberInviteRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String workspaceId = workspaceMemberInviteRequest.getWorkspaceId();
        workspaceMemberService.validateAccountHasRoleInWorkspace(currentAccountId, workspaceId, List.of(OWNER, ADMIN));
        workspaceValidator.validateWorkspaceIsNotPersonal(workspaceId);
        validateInviteNotForOwnerRole(workspaceMemberInviteRequest);
        validateAccountIsExistingMemberIfAccountPresent(workspaceMemberInviteRequest, workspaceId);
        log.info("Invite workspace member has started. currentAccountId: {}", currentAccountId);
        WorkspaceInvitationInitializeVo vo = workspaceInvitationInitializeVoConverter.convert(workspaceMemberInviteRequest, currentAccountId);
        workspaceInvitationOperationService.initializeInvitation(vo);
        return new BaseResponse();
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

    public BaseResponse joinWorkspace(String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Join workspace has started. workspaceId: {}, currentAccountId: {}", workspaceId, currentAccountId);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(workspaceId);
        validateWorkspaceJoinTypeIsPublic(workspaceDto);
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

    private void validateWorkspaceJoinTypeIsPublic(WorkspaceDto workspaceDto) {
        Optional.of(workspaceDto).map(WorkspaceDto::getSettings).map(WorkspaceSettingDto::getJoinType).filter(WorkspaceJoinType.PUBLIC::equals).orElseThrow(NoAccessException::new);
    }

    private void validateInviteNotForOwnerRole(WorkspaceMemberInviteRequest workspaceMemberInviteRequest) {
        if (OWNER.equals(workspaceMemberInviteRequest.getForRole())) {
            throw new BusinessException();
        }
    }

    private void validateAccountIsExistingMemberIfAccountPresent(WorkspaceMemberInviteRequest workspaceMemberInviteRequest, String workspaceId) {
        String accountId = accountRetrieveService.retrieveByEmail(workspaceMemberInviteRequest.getEmail()).map(AccountDto::getAccountId).orElse(null);
        if (Objects.nonNull(accountId) && workspaceMemberService.isAccountWorkspaceMember(accountId, workspaceId)) {
            throw new BusinessException();
        }
    }
}