package co.jinear.core.repository.task;

import co.jinear.core.model.entity.task.TaskFeedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskFeedItemRepository extends JpaRepository<TaskFeedItem, String> {

    List<TaskFeedItem> findAllByTaskIdAndFeedIdIsInAndPassiveIdIsNull(String taskId, List<String> feedIdList);

    Long countAllByTaskIdAndPassiveIdIsNull(String taskId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update TaskFeedItem  taskFeedItem
                set taskFeedItem.passiveId=:passiveId
                    where 
                        taskFeedItem.feedId=:feedId and 
                        taskFeedItem.passiveId is null
                """)
    void updateTaskFeedItemsAsPassive(@Param("feedId") String feedId, @Param("passiveId") String passiveId);

}
