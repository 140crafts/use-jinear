package co.jinear.core.repository.project;

import co.jinear.core.model.entity.project.ProjectPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectPostRepository extends JpaRepository<ProjectPost, String> {

    Page<ProjectPost> findAllByProjectIdAndPassiveIdIsNullOrderByCreatedDateDesc(String projectId, Pageable pageable);

    Optional<ProjectPost> findByProjectIdAndProjectPostIdAndPassiveIdIsNull(String projectId, String projectPostId);
}
