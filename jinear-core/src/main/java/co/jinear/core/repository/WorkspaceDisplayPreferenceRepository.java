package co.jinear.core.repository;

import co.jinear.core.model.entity.workspace.WorkspaceDisplayPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkspaceDisplayPreferenceRepository extends JpaRepository<WorkspaceDisplayPreference,String> {
    Optional<WorkspaceDisplayPreference> findByAccountIdAndPassiveIdIsNull(String accountId);
}
