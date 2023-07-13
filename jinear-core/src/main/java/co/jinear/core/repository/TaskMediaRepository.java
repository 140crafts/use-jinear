package co.jinear.core.repository;

import co.jinear.core.model.entity.media.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskMediaRepository extends JpaRepository<Media, String> {

    @Query("""
            select sum(media.size)
            from Task task, Media media
            where media.relatedObjectId = task.taskId and task.workspaceId = :workspaceId and task.passiveId is null and media.passiveId is null
            """)
    Long sumAllMediaSizeForWorkspace(@Param("workspaceId") String workspaceId);
}
