package co.jinear.core.repository;

import co.jinear.core.model.entity.task.TaskListEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskListEntryRepository extends JpaRepository<TaskListEntry, String> {

    Optional<TaskListEntry> findByTaskListEntryIdAndPassiveIdIsNull(String taskListEntryId);

    Long countAllByTaskListIdAndPassiveIdIsNull(String taskListId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update TaskListEntry taskListEntry
                set taskListEntry.order = taskListEntry.order + 1
                    where 
                        taskListEntry.taskListId = :taskListId and 
                        (:newOrder <= taskListEntry.order and :currentOrder < taskListEntry.order) and  
                        taskListEntry.passiveId is null
                """)
    void updateOrderUpward(@Param("taskListId") String taskListId,
                             @Param("currentOrder") int currentOrder,
                             @Param("newOrder") int newOrder);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update TaskListEntry taskListEntry
                set taskListEntry.order = taskListEntry.order - 1
                    where 
                        taskListEntry.taskListId = :taskListId and 
                        (:currentOrder < taskListEntry.order and :newOrder <= taskListEntry.order) and  
                        taskListEntry.passiveId is null
                """)
    void updateOrderDownward(@Param("taskListId") String taskListId,
                           @Param("currentOrder") int currentOrder,
                           @Param("newOrder") int newOrder);

}
