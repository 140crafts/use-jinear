package co.jinear.core.service.workspace.member;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.entity.workspace.WorkspaceMember;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.model.vo.workspace.DeleteWorkspaceMemberVo;
import co.jinear.core.model.vo.workspace.InitializeWorkspaceMemberVo;
import co.jinear.core.repository.WorkspaceMemberRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.NormalizeHelper;
import co.jinear.core.system.NumberCompareHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceMemberService {

    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final PassiveService passiveService;

    @Transactional
    public void initializeWorkspaceMember(InitializeWorkspaceMemberVo initializeWorkspaceMemberVo) {
        log.info("Initialize workspace member has started. initializeWorkspaceMemberVo: {}", initializeWorkspaceMemberVo);
        validateAccountIsNotWorkspaceMember(initializeWorkspaceMemberVo.getAccountId(), initializeWorkspaceMemberVo.getWorkspaceId());
        createWorkspaceRole(initializeWorkspaceMemberVo);
        //todo sync all teams with sync option?
        log.info("Initialize workspace member has finished.");
    }

    @Transactional
    public String deleteWorkspaceMember(String workspaceMemberId) {
        log.info("Delete workspace member has started. workspaceMemberId: {}", workspaceMemberId);
        return workspaceMemberRepository.findByWorkspaceMemberIdAndPassiveIdIsNull(workspaceMemberId)
                .map(this::deleteMember)
                .orElseThrow(NotFoundException::new);
    }

    public String deleteWorkspaceMember(DeleteWorkspaceMemberVo deleteWorkspaceMemberVo) {
        log.info("Delete workspace member has started. deleteWorkspaceMemberVo: {}", deleteWorkspaceMemberVo);
        return workspaceMemberRepository.findByAccountIdAndWorkspaceIdAndPassiveIdIsNull(deleteWorkspaceMemberVo.getAccountId(), deleteWorkspaceMemberVo.getWorkspaceId())
                .map(this::deleteMember)
                .orElseThrow(NotFoundException::new);
    }

    public boolean isAccountWorkspaceOwner(String accountId, String workspaceId) {
        return workspaceMemberRepository.countAllByAccountIdAndWorkspaceIdAndRoleAndPassiveIdIsNull(accountId, workspaceId, WorkspaceAccountRoleType.OWNER) > 0L;
    }

    public boolean isAccountWorkspaceMember(String accountId, String workspaceId) {
        return workspaceMemberRepository.existsByAccountIdAndWorkspaceIdAndPassiveIdIsNull(accountId, workspaceId);
    }

    public void validateAccountWorkspaceMember(String accountId, String workspaceId) {
        if (Boolean.FALSE.equals(isAccountWorkspaceMember(accountId, workspaceId))) {
            throw new BusinessException("workspace.not-a-member");
        }
    }

    public void validateAccountIsNotWorkspaceMember(String accountId, String workspaceId) {
        if (Boolean.TRUE.equals(isAccountWorkspaceMember(accountId, workspaceId))) {
            throw new BusinessException();
        }
    }

    public boolean doesAccountHaveWorkspaceAdminAccess(String accountId, String workspaceId) {
        log.info("Does account have workspace admin access started. workspaceId: {}, accountId: {}", workspaceId, accountId);
        Long count = workspaceMemberRepository.countAllByAccountIdAndWorkspaceIdAndRoleIsInAndPassiveIdIsNull(accountId, workspaceId, List.of(OWNER, ADMIN));
        return !NumberCompareHelper.isEquals(count, 0);
    }

    public void validateAccountHaveWorkspaceAdminAccess(String accountId, String workspaceId) {
        log.info("Validate account have workspace admin access started. workspaceId: {}, accountId: {}", workspaceId, accountId);
        if (!doesAccountHaveWorkspaceAdminAccess(accountId, workspaceId)) {
            throw new NoAccessException();
        }
    }

    public void validateAccountHasRoleInWorkspace(String accountId, String workspaceId, List<WorkspaceAccountRoleType> roleTypes) {
        log.info("Has any role for workspace started. workspaceId: {}, accountId: {}, roleTypes: {}", workspaceId, accountId, StringUtils.join(roleTypes, NormalizeHelper.COMMA_SEPARATOR));
        Long count = workspaceMemberRepository.countAllByAccountIdAndWorkspaceIdAndRoleIsInAndPassiveIdIsNull(accountId, workspaceId, roleTypes);
        if (NumberCompareHelper.isEquals(count, 0)) {
            throw new BusinessException();
        }
    }

    public void validateAccountIsNotWorkspaceOwner(String accountId, String workspaceId) {
        if (isAccountWorkspaceOwner(accountId, workspaceId)) {
            throw new BusinessException();
        }
    }

    public void removeAllMembershipsOfAnAccount(String accountId, String passiveId) {
        log.info("Remove all memberships of an account has started.");
        workspaceMemberRepository.findAllByAccountIdAndPassiveIdIsNull(accountId)
                .forEach(workspaceMember -> deleteMember(workspaceMember, passiveId));
    }

    public void validateAllHasAccess(String workspaceId, Set<String> accountIds) {
        log.info("Validate all has access has started. workspaceId: {}, accountIds: {}", workspaceId, StringUtils.join(accountIds, ","));
        Long existingCount = workspaceMemberRepository.countAllByWorkspaceIdAndAccountIdIsInAndPassiveIdIsNull(workspaceId, accountIds);
        if (Boolean.FALSE.equals(NumberCompareHelper.isEquals(existingCount.intValue(), accountIds.size()))) {
            throw new NoAccessException();
        }
    }

    public boolean checkWorkspaceMemberExistsInWorkspace(String workspaceMemberId, String workspaceId) {
        return workspaceMemberRepository.existsByWorkspaceMemberIdAndWorkspaceIdAndPassiveIdIsNull(workspaceMemberId, workspaceId);
    }

    private void createWorkspaceRole(InitializeWorkspaceMemberVo initializeWorkspaceMemberVo) {
        WorkspaceMember workspaceMember = new WorkspaceMember();
        workspaceMember.setWorkspaceId(initializeWorkspaceMemberVo.getWorkspaceId());
        workspaceMember.setAccountId(initializeWorkspaceMemberVo.getAccountId());
        workspaceMember.setRole(initializeWorkspaceMemberVo.getRole());
        workspaceMemberRepository.saveAndFlush(workspaceMember);
    }

    private String deleteMember(WorkspaceMember workspaceMember) {
        String passiveId = passiveService.createUserActionPassive();
        return deleteMember(workspaceMember, passiveId);
    }

    private String deleteMember(WorkspaceMember workspaceMember, String passiveId) {
        workspaceMember.setPassiveId(passiveId);
        workspaceMemberRepository.save(workspaceMember);
        log.info("Delete workspace member has finished. workspaceMemberId: {}, passiveId: {}", workspaceMember.getWorkspaceMemberId(), passiveId);
        return passiveId;
    }

    @Transactional
    public void transferWorkspaceOwnership(String workspaceId, String accountId, String toAccountId) {
        log.info("Transfer workspace ownership has started. workspaceId: {}, accountId: {}, toAccountId: {}", workspaceId, accountId, toAccountId);
        WorkspaceMember oldOwner = retrieveWorkspaceMember(workspaceId, accountId);
        WorkspaceMember newOwner = retrieveWorkspaceMember(workspaceId, toAccountId);
        oldOwner.setRole(MEMBER);
        newOwner.setRole(OWNER);
        workspaceMemberRepository.saveAll(List.of(oldOwner, newOwner));
        log.info("Transfer workspace ownership has completed.");
    }

    private WorkspaceMember retrieveWorkspaceMember(String workspaceId, String accountId) {
        return workspaceMemberRepository.findByAccountIdAndWorkspaceIdAndPassiveIdIsNull(accountId, workspaceId)
                .orElseThrow(NotFoundException::new);
    }
}