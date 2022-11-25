package co.jinear.core.repository;

import co.jinear.core.model.entity.workspace.WorkspaceSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceSettingRepository extends JpaRepository<WorkspaceSetting,String> {
}
