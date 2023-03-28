package co.jinear.core.service.workspace.member;

import co.jinear.core.exception.BusinessException;
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
        log.info("Initialize workspace member has finished.");
    }

    @Transactional
    public void deleteWorkspaceMember(DeleteWorkspaceMemberVo deleteWorkspaceMemberVo) {
        log.info("Delete workspace member has started. deleteWorkspaceMemberVo: {}", deleteWorkspaceMemberVo);
        workspaceMemberRepository.findByAccountIdAndWorkspaceIdAndPassiveIdIsNull(deleteWorkspaceMemberVo.getAccountId(), deleteWorkspaceMemberVo.getWorkspaceId())
                .ifPresent(workspaceMember -> {
                    deleteMember(deleteWorkspaceMemberVo, workspaceMember);
                });
    }

    public boolean isAccountWorkspaceOwner(String accountId, String workspaceId) {
        return workspaceMemberRepository.countAllByAccountIdAndWorkspaceIdAndRoleAndPassiveIdIsNull(accountId, workspaceId, WorkspaceAccountRoleType.OWNER) > 0L;
    }

    public boolean isAccountWorkspaceMember(String accountId, String workspaceId) {
        return workspaceMemberRepository.countAllByAccountIdAndWorkspaceIdAndPassiveIdIsNull(accountId, workspaceId) > 0L;
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

    private void createWorkspaceRole(InitializeWorkspaceMemberVo initializeWorkspaceMemberVo) {
        WorkspaceMember workspaceMember = new WorkspaceMember();
        workspaceMember.setWorkspaceId(initializeWorkspaceMemberVo.getWorkspaceId());
        workspaceMember.setAccountId(initializeWorkspaceMemberVo.getAccountId());
        workspaceMember.setRole(initializeWorkspaceMemberVo.getRole());
        workspaceMemberRepository.saveAndFlush(workspaceMember);
    }

    private void deleteMember(DeleteWorkspaceMemberVo deleteWorkspaceMemberVo, WorkspaceMember workspaceMember) {
        String passiveId = passiveService.createUserActionPassive(deleteWorkspaceMemberVo.getAccountId());
        workspaceMember.setPassiveId(passiveId);
        workspaceMemberRepository.save(workspaceMember);
        log.info("Delete workspace member has finished. workspaceMemberId: {}, passiveId: {}", workspaceMember.getWorkspaceMemberId(), passiveId);
    }
}