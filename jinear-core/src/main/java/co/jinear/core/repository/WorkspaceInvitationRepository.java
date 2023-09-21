package co.jinear.core.repository;

import co.jinear.core.model.entity.workspace.WorkspaceInvitation;
import co.jinear.core.model.enumtype.workspace.WorkspaceInvitationStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkspaceInvitationRepository extends JpaRepository<WorkspaceInvitation, String> {

    Optional<WorkspaceInvitation> findByWorkspaceInvitationIdAndPassiveIdIsNull(String workspaceInvitationId);

    Page<WorkspaceInvitation> findAllByWorkspaceIdAndStatusAndPassiveIdIsNullOrderByCreatedDateAsc(String workspaceId, WorkspaceInvitationStatusType status, Pageable pageable);

    Long countAllByWorkspaceIdAndStatusAndPassiveIdIsNull(String workspaceId, WorkspaceInvitationStatusType status);

    Optional<WorkspaceInvitation> findFirstByWorkspaceIdAndStatusAndEmailAndPassiveIdIsNull(String workspaceInvitationId, WorkspaceInvitationStatusType status, String email);
}
