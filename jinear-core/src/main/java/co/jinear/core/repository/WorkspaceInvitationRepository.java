package co.jinear.core.repository;

import co.jinear.core.model.entity.workspace.WorkspaceInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkspaceInvitationRepository extends JpaRepository<WorkspaceInvitation, String> {

    Optional<WorkspaceInvitation> findByWorkspaceInvitationIdAndPassiveIdIsNull(String workspaceInvitationId);
}
