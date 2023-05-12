package co.jinear.core.repository;

import co.jinear.core.model.entity.workspace.WorkspaceActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkspaceActivityRepository extends JpaRepository<WorkspaceActivity, String> {

    Page<WorkspaceActivity> findAllByWorkspaceIdAndTeamIdIsInAndPassiveIdIsNullOrderByCreatedDateDesc(String workspaceId, List<String> teamIdList, Pageable pageable);

    Page<WorkspaceActivity> findAllByTaskIdAndPassiveIdIsNullOrderByCreatedDateDesc(String taskId, Pageable pageable);

    List<WorkspaceActivity> findAllByTaskIdAndPassiveIdIsNullOrderByCreatedDateDesc(String taskId);
}
