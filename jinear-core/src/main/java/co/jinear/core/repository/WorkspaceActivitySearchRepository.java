package co.jinear.core.repository;

import co.jinear.core.model.entity.workspace.WorkspaceActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkspaceActivitySearchRepository extends JpaRepository<WorkspaceActivity, String> {

}
