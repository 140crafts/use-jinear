package co.jinear.core.repository.project;

import co.jinear.core.model.entity.project.ProjectPostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectPostCommentRepository extends JpaRepository<ProjectPostComment, String> {

    Optional<ProjectPostComment> findByProjectPostCommentIdAndPassiveIdIsNull(String projectPostCommentId);

    Page<ProjectPostComment> findAllByProjectPostIdOrderByCreatedDateAsc(String projectPostId, Pageable pageable);

    Long countAllByProjectPostIdAndPassiveIdIsNull(String projectPostId);
}
