package co.jinear.core.repository.project;

import co.jinear.core.model.entity.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, String> {

    Optional<Project> findByProjectIdAndPassiveIdIsNull(String projectId);

    boolean existsByProjectIdAndWorkspaceIdAndPassiveIdIsNull(String projectId, String workspaceId);

    Page<Project> findAllByWorkspaceIdAndPassiveIdIsNullOrderByCreatedDateDesc(String workspaceId, Pageable pageable);
}
