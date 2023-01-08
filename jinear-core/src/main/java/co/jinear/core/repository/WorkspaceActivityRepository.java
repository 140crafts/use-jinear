package co.jinear.core.repository;

import co.jinear.core.model.entity.workspace.WorkspaceActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkspaceActivityRepository extends JpaRepository<WorkspaceActivity, String> {

    List<WorkspaceActivity> findAllByTaskIdAndPassiveIdIsNullOrderByCreatedDateAsc(String taskId);
}
