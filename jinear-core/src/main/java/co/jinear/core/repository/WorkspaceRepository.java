package co.jinear.core.repository;

import co.jinear.core.model.entity.workspace.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, String> {

    List<Workspace> findAllByWorkspaceIdIsInAndPassiveIdIsNull(List<String> workspaceIds);

    Optional<Workspace> findByUsername_UsernameAndUsername_PassiveIdIsNullAndPassiveIdIsNull(String username);

    Optional<Workspace> findByWorkspaceIdAndPassiveIdIsNull(String workspaceId);
}
