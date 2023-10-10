package co.jinear.core.repository.task;

import co.jinear.core.model.entity.task.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, String> {

    Optional<Comment> findByCommentIdAndPassiveIdIsNull(String commentId);

    Page<Comment> findAllByTaskIdOrderByCreatedDateAsc(String commentId, Pageable pageable);

    long countAllByCommentIdAndOwnerIdAndPassiveIdIsNull(String commentId, String ownerId);
}
