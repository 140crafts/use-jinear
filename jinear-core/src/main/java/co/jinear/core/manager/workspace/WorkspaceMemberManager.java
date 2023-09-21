package co.jinear.core.manager.workspace;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceSettingDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.model.enumtype.workspace.WorkspaceJoinType;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.workspace.DeleteWorkspaceMemberVo;
import co.jinear.core.model.vo.workspace.InitializeWorkspaceMemberVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import co.jinear.core.validator.workspace.WorkspaceTierValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.ADMIN;
import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.OWNER;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceMemberManager {

    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final WorkspaceMemberService workspaceMemberService;
    private final WorkspaceMemberRetrieveService workspaceMemberRetrieveService;
    private final SessionInfoService sessionInfoService;
    private final PassiveService passiveService;
    private final WorkspaceTierValidator workspaceTierValidator;

    public BaseResponse joinWorkspace(String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Join workspace has started. workspaceId: {}, currentAccountId: {}", workspaceId, currentAccountId);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(workspaceId);
        validateWorkspaceJoinTypeIsPublic(workspaceDto);
        workspaceTierValidator.validateWorkspaceCanAddMember(workspaceDto);
        InitializeWorkspaceMemberVo initializeWorkspaceMemberVo = mapValues(currentAccountId, workspaceDto);
        workspaceMemberService.initializeWorkspaceMember(initializeWorkspaceMemberVo);
        //todo add workspace activity
        return new BaseResponse();
    }

    public BaseResponse leaveWorkspace(String workspaceUsername) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Leave workspace has started. workspaceUsername: {}, currentAccountId: {}", workspaceUsername, currentAccountId);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithUsername(workspaceUsername);
        workspaceMemberService.validateAccountIsNotWorkspaceOwner(currentAccountId, workspaceDto.getWorkspaceId());
        DeleteWorkspaceMemberVo deleteWorkspaceMemberVo = mapToDeleteWorkspaceMember(currentAccountId, workspaceDto.getWorkspaceId());
        String passiveId = workspaceMemberService.deleteWorkspaceMember(deleteWorkspaceMemberVo);
        passiveService.assignOwnership(passiveId, currentAccountId);
        //todo add workspace activity
        return new BaseResponse();
    }

    public BaseResponse kick(String workspaceId, String workspaceMemberId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateCurrentAccountRoleAndWorkspaceMemberWorkspaceRelation(workspaceId, workspaceMemberId, currentAccountId);
        log.info("Kick account from workspace has started. workspaceMemberId: {}, workspaceId: {}, currentAccountId: {}", workspaceMemberId, workspaceId, currentAccountId);
        String passiveId = workspaceMemberService.deleteWorkspaceMember(workspaceMemberId);
        passiveService.assignOwnership(passiveId, currentAccountId);
        //todo add workspace activity
        return new BaseResponse();
    }

    private InitializeWorkspaceMemberVo mapValues(String currentAccountId, WorkspaceDto workspaceDto) {
        InitializeWorkspaceMemberVo initializeWorkspaceMemberVo = new InitializeWorkspaceMemberVo();
        initializeWorkspaceMemberVo.setWorkspaceId(workspaceDto.getWorkspaceId());
        initializeWorkspaceMemberVo.setAccountId(currentAccountId);
        initializeWorkspaceMemberVo.setRole(WorkspaceAccountRoleType.MEMBER);
        return initializeWorkspaceMemberVo;
    }

    private DeleteWorkspaceMemberVo mapToDeleteWorkspaceMember(String accountId, String workspaceId) {
        DeleteWorkspaceMemberVo deleteWorkspaceMemberVo = new DeleteWorkspaceMemberVo();
        deleteWorkspaceMemberVo.setWorkspaceId(workspaceId);
        deleteWorkspaceMemberVo.setAccountId(accountId);
        return deleteWorkspaceMemberVo;
    }

    private void validateWorkspaceJoinTypeIsPublic(WorkspaceDto workspaceDto) {
        Optional.of(workspaceDto).map(WorkspaceDto::getSettings).map(WorkspaceSettingDto::getJoinType).filter(WorkspaceJoinType.PUBLIC::equals).orElseThrow(NoAccessException::new);
    }

    private void validateCurrentAccountRoleAndWorkspaceMemberWorkspaceRelation(String workspaceId, String workspaceMemberId, String currentAccountId) {
        workspaceMemberService.validateAccountHasRoleInWorkspace(currentAccountId, workspaceId, List.of(OWNER, ADMIN));
        workspaceMemberRetrieveService.retrieve(workspaceMemberId, workspaceId);
    }
}