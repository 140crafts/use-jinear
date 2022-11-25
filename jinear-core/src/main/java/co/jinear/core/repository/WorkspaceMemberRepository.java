package co.jinear.core.repository;

import co.jinear.core.model.entity.workspace.WorkspaceMember;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, String> {

    Long countAllByAccountIdAndWorkspaceIdAndRoleAndPassiveIdIsNull(String accountId, String workspaceId, WorkspaceAccountRoleType role);

    Long countAllByAccountIdAndWorkspaceIdAndPassiveIdIsNull(String accountId, String workspaceId);

    Long countAllByAccountIdAndWorkspaceIdAndRoleIsInAndPassiveIdIsNull(String accountId, String workspaceId, List<WorkspaceAccountRoleType> roleTypes);

    Page<WorkspaceMember> findAllByWorkspaceIdAndPassiveIdIsNull(String workspaceId, Pageable pageable);

    List<WorkspaceMember> findAllByAccountIdAndPassiveIdIsNull(String accountId);

    Optional<WorkspaceMember> findByAccountIdAndWorkspaceIdAndPassiveIdIsNull(String accountId, String workspaceId);
}